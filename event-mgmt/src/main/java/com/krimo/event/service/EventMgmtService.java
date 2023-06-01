package com.krimo.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.event.data.Event;
import com.krimo.event.dto.EventCapacityMessage;
import com.krimo.event.dto.EventDTO;
import com.krimo.event.exception.ApiRequestException;
import com.krimo.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

public interface EventMgmtService {
    String createEvent(EventDTO eventDTO);
    Event readEvent(String eventCode);
    List<Event> readAllEvents();
    void updateEvent(String eventCode, EventDTO eventDTO);
    void deleteEvent(String eventCode);
    Event eventBuild(EventDTO eventDTO);
}
@Service
@RequiredArgsConstructor
class EventMgmtServiceImpl implements EventMgmtService {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EventRepository eventRepository;

    private static final int CODE_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String createEvent(EventDTO eventDTO) {

        // serial code generator
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            stringBuilder.append(CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length())));
        }

        String eventCode = stringBuilder.toString();

        // save to database
        Event event = eventBuild(eventDTO);
        event.setEventCode(eventCode);
        eventRepository.save(event);

        // send new event to topic
        String topic = "event.maxCapacity";
        EventCapacityMessage messageObject = new EventCapacityMessage(eventCode, event.getMaxCapacity());
        String message;
        try {
            message = objectMapper.writeValueAsString(messageObject);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Unable to serialize message.");
        }
        kafkaTemplate.send(topic, message);

        return eventCode;
    }

    @Override
    public Event readEvent(String eventCode) {
        return eventRepository.findByEventCode(eventCode).isPresent()
                ? eventRepository.findByEventCode(eventCode).get()
                : null;
    }

    @Override
    public List<Event> readAllEvents() {
        return eventRepository.findAll().stream().toList();
    }

    @Override
    public void updateEvent(String eventCode, EventDTO eventDTO) {

        eventDTO.setEventCode(eventCode);

        Event event = readEvent(eventCode);

        if(event == null) {
            return;
        }

        Event updatedEvent = eventBuild(eventDTO);
        updatedEvent.setId(event.getId());
        eventRepository.save(updatedEvent);

        String topic = "event.update";
        String message;
        try {
            message = objectMapper.writeValueAsString(eventDTO);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Unable to serialize message.");
        }
        kafkaTemplate.send(topic, message);

    }

    @Override
    public void deleteEvent(String eventCode) {
        Event event = readEvent(eventCode);
        eventRepository.deleteById(event.getId());
    }

    // Reusable method - entity mapping
    @Override
    public Event eventBuild(EventDTO eventDTO) {
        return Event.builder()
                .venue(eventDTO.getVenue())
                .dateTime(eventDTO.getDateTime())
                .title(eventDTO.getTitle())
                .details(eventDTO.getDetails())
                .maxCapacity(eventDTO.getMaxCapacity())
                .organizer(eventDTO.getOrganizer())
                .build();
    }

}
