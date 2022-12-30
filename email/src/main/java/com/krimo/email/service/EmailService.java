package com.krimo.email.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krimo.email.dto.Event;
import com.krimo.email.dto.TicketList;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailFormatter emailFormatter;

    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    @KafkaListener(topics = "event-updates", groupId = "groupId")
    public void sendEventUpdates(String eventUpdates) {

        final String uri = "http://ticket:8082/api/v1/ticket/%s";


        try{
            objectMapper.registerModule(new JavaTimeModule());

            Event event = objectMapper.readValue(eventUpdates, Event.class);

            TicketList ticketList = webClient.get()
                                        .uri(String.format(uri, event.getEventCode()))
                                        .retrieve()
                                        .bodyToMono(TicketList.class)
                                        .block();

            if (ticketList != null) {
                ticketList.getTicketList().forEach(ticket -> emailFormatter.emailFormatter(ticket.getCustomerEmail(), event));
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
