package com.krimo.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.event.data.Event;
import com.krimo.event.data.EventCapacity;
import com.krimo.event.data.EventCapacityKey;
import com.krimo.event.data.Section;
import com.krimo.event.dto.EventDTO;
import com.krimo.event.exception.ApiRequestException;
import com.krimo.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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
    private final EventCapacityService eventCapacityService;
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
        eventRepository.save(event);

        // persist event max capacity to cassandra
        for (Map.Entry<Section, Integer> entry: eventDTO.getMaxCapacity().entrySet()) {
            eventCapacityService.setEventCapacity(
                    new EventCapacity(
                            new EventCapacityKey(
                                    entry.getKey().name(),     // partition key Section as String
                                    eventCode                  // clustering key
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
            eventDTO.setAttendeeCount(fetchMaxCapacity(event.getEventCode()));

            eventDTOList.add(eventDTO);
        }

        return eventDTOList;
    }

    @Override
    public void updateEvent(String eventCode, EventDTO eventDTO) {

        Event existingEvent = eventBuild(readEvent(eventCode));

        Event updatedEvent = eventBuild(eventDTO);
        updatedEvent.setEventCode(eventCode);
        updatedEvent.setId(existingEvent.getId());

        eventRepository.save(updatedEvent);

        try {
            String eventUpdate = objectMapper.writeValueAsString(updatedEvent);
            kafkaTemplate.send("event-updates", eventUpdate);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Failed to serialize updated event. PLease try again.");
        }

    }

    @Override
    public void deleteEvent(String eventCode) {
        eventRepository.deleteById(eventBuild(readEvent(eventCode)).getId());
        eventCapacityService.deleteEvent(eventCode);
    }

    // Fetch event capacity from cassandra
    public HashMap<Section, Integer> fetchMaxCapacity(String eventCode) {
        HashMap<Section, Integer> maxCapacityMap = new HashMap<>();

        for (EventCapacity eventCapacity: eventCapacityService.getEventCapacity(eventCode)) {
            maxCapacityMap.put(
                    Section.valueOf(eventCapacity.getKey().getSection()),
                    eventCapacity.getMaxCapacity()
            );
        }
        return maxCapacityMap;
    }

    public HashMap<Section, Integer> fetchAttendeeCount(String eventCode) {
        HashMap<Section, Integer> getAttendeeMap = new HashMap<>();

        for (EventCapacity eventCapacity: eventCapacityService.getEventCapacity(eventCode)) {
            getAttendeeMap.put(
                    Section.valueOf(eventCapacity.getKey().getSection()),
                    eventCapacity.getAttendeeCount()
            );
        }
        return getAttendeeMap;
    }

    // Reusable method - entity mapping
    public Event eventBuild(EventDTO eventDTO) {
        return Event.builder()
                .name(eventDTO.getName())
                .venue(eventDTO.getVenue())
                .dateTime(eventDTO.getDateTime())
                .details(eventDTO.getDetails())
                .organizer(eventDTO.getOrganizer())
                .build();
    }

    public EventDTO eventDTOBuild(Event event) {
        return EventDTO.builder()
                .name(event.getName())
                .venue(event.getVenue())
                .dateTime(event.getDateTime())
                .details(event.getDetails())
                .organizer(event.getOrganizer())
                .build();
    }

}
