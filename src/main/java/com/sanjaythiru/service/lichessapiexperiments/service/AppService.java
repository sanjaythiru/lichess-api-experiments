package com.sanjaythiru.service.lichessapiexperiments.service;

import com.sanjaythiru.service.lichessapiexperiments.domain.Game;
import com.sanjaythiru.service.lichessapiexperiments.domain.User;
import com.sanjaythiru.service.lichessapiexperiments.domain.enums.SpeedEnum;
import com.sanjaythiru.service.lichessapiexperiments.dto.TopOpeningsDto;
import com.sanjaythiru.service.lichessapiexperiments.lichessdto.GameLichessDto;
import com.sanjaythiru.service.lichessapiexperiments.lichessdto.LeaderboardLichessDto;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppService.class);

    @Autowired
    private WebClient webClient;

    @Autowired
    private ModelMapper userModelMapper;

    @Value("${lichess.api.leaderboard.baseurl}")
    private String lichessLeaderboardBaseUrl;

    @Value("${lichess.api.user.game.baseurl}")
    private String lichessUserGameBaseUrl;

    public TopOpeningsDto getTopOpenings(SpeedEnum speed, Long nbr) {
         List<User> leaderBoard = getLeaderboard(speed, nbr);
        if(leaderBoard != null) {
            Map<String, Long> openingsAggregate = new HashMap<>();
            leaderBoard.forEach(user -> {
                Game currentGame = getCurrentGame(user);
                if(currentGame != null) {
                    Long count = openingsAggregate.getOrDefault(currentGame.getOpeningEco(), 0L);
                    openingsAggregate.put(currentGame.getOpeningEco(), count + 1);
                }
            });
            return new TopOpeningsDto(openingsAggregate);

        } else {
            LOGGER.warn("Leaderboard is empty. Something went wrong");
            return null;
        }
    }

    private List<User> getLeaderboard(SpeedEnum speed, Long nbr) {
        String url = lichessLeaderboardBaseUrl + "/" + nbr  + "/" + speed.name;
        URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();
        LeaderboardLichessDto response = webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(LeaderboardLichessDto.class)
                .block();

        if(response == null || response.getUsers() == null) {
            LOGGER.warn("Leaderboard response from Lichess is null");
            return null;
        }
        LOGGER.trace("Leaderboard response from Lichess : {}", response);
        List<User> leaderboard = new ArrayList<>();
        response.getUsers().forEach(userLichessDto -> {
            leaderboard.add(userModelMapper.map(userLichessDto, User.class));
        });
        LOGGER.debug("Leaderboard : {}", leaderboard);
        return leaderboard;
    }

    private Game getCurrentGame(User user) {
        String url = lichessUserGameBaseUrl + "/" + user.getUsername() + "/current-game";
        URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();
        GameLichessDto response = webClient.get()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(GameLichessDto.class)
                .block();

        if(response == null) {
            LOGGER.warn("Response is null");
            return null;
        }
        if(response.getOpening() == null) {
            LOGGER.warn("Opening is null, possibly the user has aborted the game without playing");
            return null;
        }

        // TODO Use modelmapper to convert to Game object later
        return new Game(response.getId(), response.getSpeed(), response.getOpening().getName(), response.getOpening().getEco());
    }
}
