package com.krimo.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.notification.dto.PurchaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
class NotificationService {

    @Value("${servers.account-service}")
    private String accountService;
    @Value("${servers.event-service}")
    private String eventService;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final MessageSenderService senderService;

    @KafkaListener(topics = "ticket-purchase")
    public void sendPurchaseConfirmation(String ticketPurchase) throws JsonProcessingException {

        PurchaseEvent message = objectMapper.readValue(ticketPurchase, PurchaseEvent.class);
        log.info("Purchase Event: " + message);

        Mono<String> emailMono = webClient
                .get()
                .uri(String.format("%s/api/v3/accounts/%d/email", accountService, message.userId()))
                .retrieve()
                .bodyToMono(String.class);

        Mono<String> eventMono = webClient
                .get()
                .uri(String.format("%s/api/v3/event-query/%d/name", eventService, message.eventId()))
                .retrieve()
                .bodyToMono(String.class);

        Mono.zip(emailMono, eventMono)
                .doOnNext(tuple -> {
                    String email = tuple.getT1();
                    String event = tuple.getT2();
                    log.info(String.format("RECIPIENT: %1s EVENT: %2s", email, event));
                    senderService.sendMessage(email, event);
                })
                .subscribe();
    }
}
