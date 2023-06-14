package com.krimo.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.event.data.Event;
import com.krimo.event.data.EventCapacity;
import com.krimo.event.data.EventCapacityKey;
import com.krimo.event.data.Section;
import com.krimo.event.dto.EventDTO;
import com.krimo.event.exception.ApiRequestException;
import com.krimo.event.repository.EventCapacityRepository;
import com.krimo.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

public interface EventService {

    String createEvent(EventDTO eventDTO);
    EventDTO readEvent(String eventCode);
    List<EventDTO> readAllEvents();
    void updateEvent(String eventCode, EventDTO eventDTO);
    void deleteEvent(String eventCode);

}

@Service
@RequiredArgsConstructor
class EventServiceImpl implements EventService{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EventRepository eventRepository;
    private final EventCapacityRepository eventCapacityRepository;
    private final ObjectMapper objectMapper;

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
        event.setCreatedAt(LocalDateTime.now());
        eventRepository.save(event);

        // persist event max capacity to cassandra
        for (Map.Entry<Section, Integer> entry: eventDTO.getMaxCapacity().entrySet()) {
            eventCapacityRepository.save(
                    new EventCapacity(
                            new EventCapacityKey(
                                    eventCode,                 // partition key event code
                                    entry.getKey().name()      // clustering key Section as String
                            ),
                            entry.getValue(),                  // max capacity per
                            0                                  // attendee count
                    )
            );
        }

        return eventCode;
    }

    @Override
    public EventDTO readEvent(String eventCode) {
        Event event = eventRepository.findByEventCode(eventCode).orElseThrow();
        EventDTO eventDTO = eventDTOBuild(event);
        eventDTO.setMaxCapacity(fetchMaxCapacity(eventCode));
        eventDTO.setAttendeeCount(fetchAttendeeCount(eventCode));
        return eventDTO;
    }

    @Override
    public List<EventDTO> readAllEvents() {
        List<EventDTO> eventDTOList = new ArrayList<>();
        for (Event event: eventRepository.findAll()) {
            EventDTO eventDTO = eventDTOBuild(event);
            eventDTO.setMaxCapacity(fetchMaxCapacity(event.getEventCode()));
            eventDTO.setAttendeeCount(fetchAttendeeCount(event.getEventCode()));

            eventDTOList.add(eventDTO);
        }

        return eventDTOList;
    }

    @Override
    public void updateEvent(String eventCode, EventDTO eventDTO) {

        Event event = eventRepository.findByEventCode(eventCode).orElseThrow();

        if (eventDTO.getName() != null) { event.setName(eventDTO.getName()); }
        if (eventDTO.getVenue() != null) { event.setVenue(eventDTO.getVenue()); }
        if (eventDTO.getDetails() != null) { event.setDetails(eventDTO.getDetails()); }
        if (eventDTO.getDateTime() != null) { event.setDateTime(eventDTO.getDateTime()); }

        eventRepository.save(event);

        try {
            String eventUpdate = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("event-updates", eventUpdate);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Failed to serialize updated event. PLease try again.");
        }

    }

    @Override
    public void deleteEvent(String eventCode) {
        eventRepository.deleteById(eventBuild(readEvent(eventCode)).getId());
        eventCapacityRepository.deleteByEventCode(eventCode);
    }

    // Fetch event capacity from cassandra
    private HashMap<Section, Integer> fetchMaxCapacity(String eventCode) {
        HashMap<Section, Integer> maxCapacityMap = new HashMap<>();

        for (EventCapacity eventCapacity: eventCapacityRepository.findByEventCode(eventCode)) {
            maxCapacityMap.put(
                    Section.valueOf(eventCapacity.getKey().getSection()),
                    eventCapacity.getMaxCapacity()
            );
        }
        return maxCapacityMap;
    }

    private HashMap<Section, Integer> fetchAttendeeCount(String eventCode) {
        HashMap<Section, Integer> getAttendeeMap = new HashMap<>();

        for (EventCapacity eventCapacity: eventCapacityRepository.findByEventCode(eventCode)) {
            getAttendeeMap.put(
                    Section.valueOf(eventCapacity.getKey().getSection()),
                    eventCapacity.getAttendeeCount()
            );
        }
        return getAttendeeMap;
    }

    // Reusable method - entity mapping
    private Event eventBuild(EventDTO eventDTO) {
        return Event.builder()
                .name(eventDTO.getName())
                .venue(eventDTO.getVenue())
                .dateTime(eventDTO.getDateTime())
                .details(eventDTO.getDetails())
                .organizer(eventDTO.getOrganizer())
                .createdAt(eventDTO.getCreatedAt())
                .build();
    }

    private EventDTO eventDTOBuild(Event event) {
        return EventDTO.builder()
                .eventCode(event.getEventCode())
                .name(event.getName())
                .venue(event.getVenue())
                .dateTime(event.getDateTime())
                .details(event.getDetails())
                .organizer(event.getOrganizer())
                .createdAt(event.getCreatedAt())
                .build();
    }

}
