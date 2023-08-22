package com.krimo.event_command.service;

import com.krimo.event_command.data.Event;
import com.krimo.event_command.dto.EventDTO;
import com.krimo.event_command.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface EventCommandService {

    Long createEvent(EventDTO eventDTO);
    void updateEvent(Long id, EventDTO eventDTO);

}

@Service
@RequiredArgsConstructor
class EventCommandServiceImpl implements EventCommandService {

    private final EventRepository eventRepository;

    @Override
    public Long createEvent(EventDTO eventDTO) {

        Event event = Event.create(
                        eventDTO.getName(),
                        eventDTO.getDescription(),
                        eventDTO.getVenue(),
                        eventDTO.getDateTime(),
                        eventDTO.getCreatedBy());

        return eventRepository.saveAndFlush(event).getId();
    }

    @Override
    public void updateEvent(Long id, EventDTO eventDTO) {

        Event event = eventRepository.findById(id).orElseThrow();

        if (eventDTO.getVenue() != null) event.setVenue(eventDTO.getVenue());
        if (eventDTO.getDescription() != null) event.setDescription(eventDTO.getDescription());
        if (eventDTO.getDateTime()!= null) event.setDateTime(eventDTO.getDateTime());
        if (eventDTO.getIsCanceled()!= null) event.setIsCanceled(eventDTO.getIsCanceled());

        eventRepository.save(event);
    }

    // Reusable method - entity to dto mapping

    private EventDTO mapToEventDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .venue(event.getVenue())
                .dateTime(event.getDateTime())
                .description(event.getDescription())
                .createdBy(event.getCreatedBy())
                .createdAt(event.getCreatedAt())
                .isCanceled(event.getIsCanceled())
                .build();
    }
}
