package com.sanjaythiru.service.lichessapiexperiments.utilities;

import com.sanjaythiru.service.lichessapiexperiments.lichessdto.IDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

public class LichessRequestUtility {

    private static final Logger LOGGER = LoggerFactory.getLogger(LichessRequestUtility.class);

    public static <T extends IDto> T makeGetRequest(WebClient webClient, URI uri, Class<T> clazz) {
        LOGGER.debug("Requesting Lichess API - {}", uri);
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeToMono(response -> {
                    if(HttpStatus.OK.equals(response.statusCode())) {
                        return response.bodyToMono(clazz);
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                })
                // TODO : Implement Retry policies
                //  - Keep retrying after 10 seconds if received HttpStatus 429 ( TOO MANY REQUESTS )
                // .retryWhen(RetrySpec)
                .block();
    }
}
