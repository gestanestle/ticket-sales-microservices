package com.krimo.event.service;

import com.krimo.event.data.Event;
import com.krimo.event.data.Section;
import com.krimo.event.dto.EventDTO;
import com.krimo.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

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

        eventRepository.save(updatedEvent);
    }

    public Collection<Section> addAttendee(String eventCode, Section section) {

        // Sections that have already reached max capacity
        Collection<Section> fullSections = new ArrayList<>();

        Event event = eventRepository.findByEventCode(eventCode).get();

        HashMap<Section, Integer> seatAttendee = event.getRegisteredAttendees() == null ?  new HashMap<>() : event.getRegisteredAttendees();

        if(seatAttendee.containsKey(section)) {
            seatAttendee.put(section, seatAttendee.get(section)+1);
        } else
            seatAttendee.put(section, 1);

        event.setRegisteredAttendees(seatAttendee);
        eventRepository.save(event);

        return fullSections;
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
