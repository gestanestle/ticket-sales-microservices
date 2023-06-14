package com.krimo.email.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.email.dto.Event;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

public interface EmailService {
    void sendEventUpdates(String eventUpdates);
}
@Service
@RequiredArgsConstructor
class EmailServiceImpl implements EmailService {

    private final EmailFormatter emailFormatter;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    @KafkaListener(topics = "event-updates", groupId = "event-updates-consumer-group")
    @Override public void sendEventUpdates(String eventUpdates) {

        // TODO: Change hostname to ticket
        final String uri = "http://localhost:8082/api/v2/ticket/%s/emails";
        Event event;
        try {
            event = objectMapper.readValue(eventUpdates, Event.class);
        } catch (JsonProcessingException e) {
            throw new ApiException("Failed to deserialize event-updates message.");
        }

        ParameterizedTypeReference<Set<String>> responseType = new ParameterizedTypeReference<>() {};
        Set<String> emailsList = webClient.get()
                .uri(String.format(uri, event.getEventCode()))
                .retrieve()
                .bodyToMono(responseType)
                .block();


        if (emailsList != null) {
            emailsList.forEach(email -> emailFormatter.formatMail(email, event));
        }
    }
}
