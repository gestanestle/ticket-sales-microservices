package com.krimo.event_command.service;

import com.krimo.event_command.data.Event;
import com.krimo.event_command.data.EventFactory;
import com.krimo.event_command.dto.EventDTO;
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

    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    @Autowired
    private EventCommandServiceImpl eventService;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;

    Event event;
    EventDTO eventDTO;

    @BeforeEach
    void setUp() {

        eventService = new EventCommandServiceImpl(eventRepository);

        event = EventFactory.eventInit();
        event.setId(1L);
        eventDTO = EventFactory.eventDTOInit();
    }

    @Test
    void createEvent() {
        when(eventRepository.saveAndFlush(eventCaptor.capture())).thenReturn(event);

        eventService.createEvent(eventDTO);

        assertThat(eventCaptor.getValue()).usingRecursiveComparison()
                .ignoringFields("id", "createdAt").isEqualTo(event);
        verify(eventRepository).saveAndFlush(eventCaptor.capture());
    }

    @Test
    void updateEvent(){
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        eventDTO.setVenue("New Venue");
        eventDTO.setDescription("New Description");

        eventService.updateEvent(1L, eventDTO);

        verify(eventRepository).save(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getVenue()).isEqualTo("New Venue");
        assertThat(eventCaptor.getValue().getDescription()).isEqualTo("New Description");
    }

}