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
        return eventRepository.findByEventCode(eventCode).get();
    }

    public List<Event> readAllEvents() {
        return eventRepository.findAll().stream().toList();
    }

    public void updateEvent(String eventCode, EventDTO eventDTO) {

        Event event = eventRepository.findByEventCode(eventCode).get();

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

        // Sections that have already reached max capacity
        Collection<Section> fullSectionsCollection = new ArrayList<>();

        Event event = eventRepository.findByEventCode(eventCode).get();

        HashMap<Section, Integer> seatAttendee = event.getRegisteredAttendees() == null ?  new HashMap<>() : event.getRegisteredAttendees();

        HashMap<Section, Integer> maxCapacity = event.getMaxCapacity();

        for (Section key: seatAttendee.keySet()) {
            if (seatAttendee.get(key) >= maxCapacity.get(key)) {
                fullSectionsCollection.add(key);
            }
        }

        if (fullSectionsCollection.contains(section)) {
            throw new ApiRequestException("Section is already full.");
        }

        if (seatAttendee.containsKey(section)) {
            AtomicInteger integer = new AtomicInteger(seatAttendee.get(section));
            seatAttendee.put(section, integer.incrementAndGet());
        } else
            seatAttendee.put(section, 1);

        event.setRegisteredAttendees(seatAttendee);
        eventRepository.save(event);

    }

    public void deleteEvent(String eventCode) {

        Long id = eventRepository.findByEventCode(eventCode).get().getId();

        eventRepository.deleteById(id);
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
