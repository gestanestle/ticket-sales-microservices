package com.krimo.email.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.email.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public interface EmailSenderService {
    void sendEventUpdates(String eventUpdates);
}

@Service
@RequiredArgsConstructor
class EmailSenderServiceImpl implements EmailSenderService {

    private final EmailFormatter emailFormatter;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;



    @KafkaListener(topics = "event.update", groupId = "event-updates-consumer-group")
    public void sendEventUpdates(String updatedEvent) {
        final String uri = "http://localhost:8082/api/v2/ticket/emails/%s";
        EventDTO event;
        try {
            event = objectMapper.readValue(updatedEvent, EventDTO.class);
        } catch (JsonProcessingException e) {
            throw new ApiException(e.getMessage());
        }

        ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<>() {};
        List<String> emailsList = webClient.get()
                .uri(String.format(uri, event.getEventCode()))
                .retrieve()
                .bodyToMono(responseType)
                .block();


        if (emailsList != null) {
            emailsList.forEach(email -> emailFormatter.formatMail(email, event));
        }
    }
}