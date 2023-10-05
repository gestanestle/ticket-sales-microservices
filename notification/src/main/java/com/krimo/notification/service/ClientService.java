package com.krimo.notification.service;

import com.krimo.notification.exception.UserProfileServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public interface ClientService {

    String getEmail(long id);
}

@Service
@RequiredArgsConstructor
class WebClientServiceImpl implements ClientService {

    private final WebClient webClient;

    @Override
    public String getEmail(long id) {
        return webClient.get()
                .uri("/api/v2/user-profiles/{id}/email", id)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),
                        error -> Mono.error(new UserProfileServiceException(
                                String.format("Failed to retrieve email address of User %d.", id))))
                .bodyToMono(String.class)
                .block();
    }
}
