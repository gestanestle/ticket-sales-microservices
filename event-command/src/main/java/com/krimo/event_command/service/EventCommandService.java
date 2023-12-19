package com.krimo.event_command.service;

import com.krimo.event_command.data.Event;
import com.krimo.event_command.data.EventOutbox;
import com.krimo.event_command.data.types.EventOutboxPayload;
import com.krimo.event_command.data.types.Status;
import com.krimo.event_command.dto.EventDTO;
import com.krimo.event_command.exception.ApiRequestException;
import com.krimo.event_command.repository.EventOutboxRepository;
import com.krimo.event_command.repository.EventRepository;
import com.krimo.event_command.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

public interface EventCommandService {

    Long createEvent(EventDTO eventDTO);
    void updateEvent(Long id, EventDTO eventDTO);
    void deleteEvent(Long eventId);
}

@Service
@RequiredArgsConstructor @Slf4j
@Transactional(isolation = Isolation.SERIALIZABLE)
class EventCommandServiceImpl implements EventCommandService {

    @Value("${event.outbox.topic}")
    private String outboxTopic;

    private final EventRepository eventRepository;
    private final EventOutboxRepository outboxRepository;

    @Override
    public Long createEvent(EventDTO eventDTO) {

        Event event = Event.create(
                eventDTO.name(), eventDTO.banner(), eventDTO.description(), eventDTO.venue(),
                eventDTO.startDateTime(), eventDTO.endDateTime(), eventDTO.organizer(), eventDTO.tags()
        );

        Long eventId = eventRepository.saveAndFlush(event).getId();
        EventOutboxPayload eventOutboxPayload = new EventOutboxPayload(eventId, true);
        String outboxPayload = Utils.writeToString(eventOutboxPayload);
        outboxRepository.save(EventOutbox.publish(outboxTopic, outboxPayload));

        log.info("Event ID: " + eventId + ", Status: " + "CREATED");

        return eventId;

    }

    @Override
    public void updateEvent(Long eventId, EventDTO eventDTO) {

        Event event = eventRepository.findById(eventId).orElseThrow( ()
                -> new ApiRequestException(HttpStatus.NOT_FOUND, "Event does not exist."));

        if (Objects.nonNull(eventDTO.name())) event.setName(eventDTO.name());
        if (Objects.nonNull(eventDTO.banner())) event.setName(eventDTO.banner());
        if (Objects.nonNull(eventDTO.description())) event.setName(eventDTO.description());
        if (Objects.nonNull(eventDTO.venue())) event.setName(eventDTO.venue());

        if (Objects.nonNull(eventDTO.startDateTime())) event.setStartDateTime(eventDTO.startDateTime());
        if (Objects.nonNull(eventDTO.endDateTime())) event.setEndDateTime(eventDTO.endDateTime());
        if (Objects.nonNull(eventDTO.organizer())) event.setOrganizer(eventDTO.organizer());
        if (Objects.nonNull(eventDTO.tags())) event.setTags(eventDTO.tags());

        if (Objects.nonNull(eventDTO.status())) {
            log.info("Event ID: " + eventId + ", Status: " + eventDTO.status());
            event.setStatus(eventDTO.status());
            boolean isActive = eventDTO.status().equals(Status.ACTIVE);
            EventOutboxPayload eventOutboxPayload = new EventOutboxPayload(eventId, isActive);
            String outboxPayload = Utils.writeToString(eventOutboxPayload);
            outboxRepository.save(EventOutbox.publish(outboxTopic, outboxPayload));
        }

        eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) throw new ApiRequestException(HttpStatus.BAD_REQUEST, "Event doesn't exist");
        eventRepository.deleteById(eventId);
        EventOutboxPayload eventOutboxPayload = new EventOutboxPayload(eventId, false);
        String outboxPayload = Utils.writeToString(eventOutboxPayload);
        outboxRepository.save(EventOutbox.publish(outboxTopic, outboxPayload));
        log.info("Event ID: " + eventId + ", Status: " + "DELETED");
    }


}
