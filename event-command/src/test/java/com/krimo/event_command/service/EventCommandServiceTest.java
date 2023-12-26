package com.krimo.event_command.service;

import com.krimo.event_command.data.Event;
import com.krimo.event_command.data.EventFactory;
import com.krimo.event_command.data.EventOutbox;
import com.krimo.event_command.dto.EventDTO;
import com.krimo.event_command.repository.EventOutboxRepository;
import com.krimo.event_command.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventCommandServiceTest {

    @Mock private EventRepository eventRepository;
    @Mock private EventOutboxRepository outboxRepository;
    @InjectMocks
    @Autowired
    private EventCommandServiceImpl eventService;

    @Captor private ArgumentCaptor<Event> eventCaptor;

    Event event;


    @BeforeEach
    void setUp() {

        eventService = new EventCommandServiceImpl(eventRepository, outboxRepository);

        event = EventFactory.eventInit();
        event.setId(1L);
    }

    @Test
    void createEvent() {
        EventDTO eventDTO = EventFactory.eventDTOInit();

        when(eventRepository.saveAndFlush(eventCaptor.capture())).thenReturn(event);
        eventService.createEvent(eventDTO);

        assertThat(eventCaptor.getValue()).usingRecursiveComparison()
                .ignoringFields("id", "createdAt").isEqualTo(event);
        verify(eventRepository).saveAndFlush(eventCaptor.capture());
        verify(outboxRepository, times(1)).save(any());
    }

    @Test
    void updateEvent(){
        EventDTO eventUpdate = EventFactory.eventDTOUpdate("New Venue", "New Description");

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        eventService.updateEvent(1L, eventUpdate);

        verify(eventRepository).save(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getVenue()).isEqualTo("New Venue");
        assertThat(eventCaptor.getValue().getDescription()).isEqualTo("New Description");
    }

    @Test
    void deleteEvent(){
        when(eventRepository.existsById(anyLong())).thenReturn(true);
        eventService.deleteEvent(1L);

        verify(eventRepository, times(1)).deleteById(anyLong());
        verify(outboxRepository, times(1)).save(any(EventOutbox.class));
    }

}