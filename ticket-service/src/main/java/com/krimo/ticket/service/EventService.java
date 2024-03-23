package com.krimo.ticket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.ticket.dto.broker_msg.EventInbox;
import com.krimo.ticket.dto.broker_msg.EventInboxPayload;
import com.krimo.ticket.models.Event;
import com.krimo.ticket.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
@Slf4j
class EventService {

    private final EventRepository eventRepository;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "outbox.event.event_status")
    public void persistEvent(String msg) {
        log.info("Kafka message: " + msg);
        EventInboxPayload payload = null;
        try {
            EventInbox inbox = mapper.readValue(msg, EventInbox.class);
            payload = mapper.readValue(inbox.payload(), EventInboxPayload.class);
        }catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        if (payload == null) {
            log.info("Empty event.");
            return;
        }

        boolean isPresent = eventRepository.findById(payload.eventId()).isPresent();
        Event event;
        if (isPresent) {
            event = eventRepository.getReferenceById(payload.eventId());
            event.setIsActive(payload.isActive());
        }else {
            event = new Event(payload.eventId(), payload.isActive(), new HashSet<>());
        }

        eventRepository.save(event);
    }

}
