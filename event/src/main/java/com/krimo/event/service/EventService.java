package com.krimo.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.event.data.Event;
import com.krimo.event.data.Section;
import com.krimo.event.dto.EventDTO;
import com.krimo.event.exception.ApiRequestException;
import com.krimo.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class EventService{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    @Value("${event.topic.name}")
    String topic;

    public String createEvent(EventDTO eventDTO) {

        String eventCode = UUID.randomUUID().toString();

        Event event = eventBuild(eventDTO);
        event.setEventCode(eventCode);

        eventRepository.save(event);

        return eventCode;
    }

    public Event readEvent(String eventCode) {
        return eventRepository.findByEventCode(eventCode).isPresent()
                ? eventRepository.findByEventCode(eventCode).get()
                : null;
    }

    public List<Event> readAllEvents() {
        return eventRepository.findAll().stream().toList();
    }

    public void updateEvent(String eventCode, EventDTO eventDTO) {

        Event event = readEvent(eventCode);

        if(event == null) {
            return;
        }

        Event updatedEvent = eventBuild(eventDTO);
        updatedEvent.setEventCode(eventCode);
        updatedEvent.setId(event.getId());
        updatedEvent.setRegisteredAttendees(event.getRegisteredAttendees());

        eventRepository.save(updatedEvent);

        try {
            String eventUpdate = objectMapper.writeValueAsString(updatedEvent);
            kafkaTemplate.send(topic, eventUpdate);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public synchronized void addAttendee(String eventCode, Section section) {

        Event event = readEvent(eventCode);

        if(event == null) {
            return;
        }

        HashMap<Section, Integer> registeredAttendees = event.getRegisteredAttendees() == null ?  new HashMap<>() : event.getRegisteredAttendees();

        HashMap<Section, Integer> maxCapacity = event.getMaxCapacity();

        for (Section key: registeredAttendees.keySet()) {
            if (registeredAttendees.get(key) >= maxCapacity.get(key)) {
                throw new ApiRequestException("Section is already full.");
            }
        }

        if (registeredAttendees.containsKey(section)) {
            AtomicInteger integer = new AtomicInteger(registeredAttendees.get(section));
            registeredAttendees.put(section, integer.incrementAndGet());
        } else
            registeredAttendees.put(section, 1);

        event.setRegisteredAttendees(registeredAttendees);
        eventRepository.save(event);
    }

    public void deleteEvent(String eventCode) {

        eventRepository.deleteById(readEvent(eventCode).getId());
    }

    // Reusable method - entity mapping
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
