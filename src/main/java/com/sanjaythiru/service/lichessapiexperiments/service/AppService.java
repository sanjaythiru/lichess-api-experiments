package com.sanjaythiru.service.lichessapiexperiments.service;

import com.sanjaythiru.service.lichessapiexperiments.domain.Game;
import com.sanjaythiru.service.lichessapiexperiments.domain.User;
import com.sanjaythiru.service.lichessapiexperiments.domain.enums.SpeedEnum;
import com.sanjaythiru.service.lichessapiexperiments.dto.TopOpeningsDto;
import com.sanjaythiru.service.lichessapiexperiments.lichessdto.GameLichessDto;
import com.sanjaythiru.service.lichessapiexperiments.lichessdto.LeaderboardLichessDto;
import com.sanjaythiru.service.lichessapiexperiments.utilities.LichessRequestUtility;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppService.class);

    private static final String PATH_DELIMITER = "/";

    @Autowired
    private WebClient webClient;

    @Autowired
    private ModelMapper userModelMapper;

    @Value("${lichess.api.leaderboard.baseurl}")
    private String lichessLeaderboardBaseUrl;

    @Value("${lichess.api.user.game.baseurl}")
    private String lichessUserGameBaseUrl;

    public TopOpeningsDto getTopOpenings(SpeedEnum speed, Long nbr) {
        if(speed == null) {
            LOGGER.debug("BAD REQUEST, Incorrect speed type in request");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect speed type in request");
        }
         List<User> leaderBoard = getLeaderboard(speed, nbr);
        if(leaderBoard != null) {
            Map<String, Long> openingsAggregate = new HashMap<>();
            AtomicInteger userNo = new AtomicInteger(1);
            leaderBoard.forEach(user -> {
                LOGGER.trace("{} - Processing user {}", userNo, user.getUsername());
                Game currentGame = getCurrentGame(user);
                if(currentGame != null) {
                    Long count = openingsAggregate.getOrDefault(currentGame.getOpeningEco(), 0L);
                    openingsAggregate.put(currentGame.getOpeningEco(), count + 1);
                }
                LOGGER.debug("{} - Processed user {}", userNo, user.getUsername());
                LOGGER.trace("OpeningsAggregate {}", openingsAggregate);
                userNo.getAndIncrement();
            });
            return new TopOpeningsDto(openingsAggregate);

        } else {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Something went wrong. Please try again");
        }
    }

    private List<User> getLeaderboard(SpeedEnum speed, Long nbr) {
        String url = lichessLeaderboardBaseUrl + PATH_DELIMITER + nbr  + PATH_DELIMITER + speed.name;
        URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();
        LeaderboardLichessDto leaderboardResponse =
                LichessRequestUtility.makeGetRequest(webClient, uri, LeaderboardLichessDto.class);
        if(leaderboardResponse == null || leaderboardResponse.getUsers() == null) {
            LOGGER.warn("Leaderboard response from Lichess is null");
            return null;
        }
        LOGGER.trace("Leaderboard response from Lichess : {}", leaderboardResponse);
        List<User> leaderboard = new ArrayList<>();
        leaderboardResponse.getUsers().forEach(userLichessDto -> {
            leaderboard.add(userModelMapper.map(userLichessDto, User.class));
        });
        LOGGER.debug("Leaderboard : {}", leaderboard);
        return leaderboard;
    }

    private Game getCurrentGame(User user) {
        if(user == null) {
            LOGGER.debug("User is null, game request could not be sent");
            return null;
        }
        String url = lichessUserGameBaseUrl + PATH_DELIMITER + user.getUsername() + PATH_DELIMITER + "current-game";
        URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();
        GameLichessDto gameResponse =
                LichessRequestUtility.makeGetRequest(webClient, uri, GameLichessDto.class);
        LOGGER.debug("Game response for user {} is {}", user.getUsername(), gameResponse);
        if(gameResponse == null) {
            LOGGER.warn("Game response is null");
            return null;
        }
        if(gameResponse.getOpening() == null) {
            LOGGER.warn("Opening from the response of user - {} is null. Ignoring", user.getUsername());
            // TODO Derive the opening from the moves played in the game
            return null;
        }

        // TODO Use modelmapper to convert to Game object later
        return new Game(gameResponse.getId(), gameResponse.getSpeed(), gameResponse.getOpening().getName(), gameResponse.getOpening().getEco());
    }
}
