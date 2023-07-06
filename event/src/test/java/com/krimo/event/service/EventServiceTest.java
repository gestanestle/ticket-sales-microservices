package com.krimo.event.service;

import com.krimo.event.data.Event;
import com.krimo.event.data.EventTest;
import com.krimo.event.dto.EventDTO;
import com.krimo.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    @Autowired
    private EventServiceImpl eventService;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;

    Event event;
    EventDTO eventDTO;

    @BeforeEach
    void setUp() {

        eventService = new EventServiceImpl(eventRepository);

        event = EventTest.eventInit();
        event.setId(1L);
        eventDTO = EventTest.eventDTOInit();
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
    void getEvent() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        assertThat(eventService.getEvent(anyLong())).usingRecursiveComparison()
                .ignoringFields("createdAt").isEqualTo(eventDTO);
    }

    @Test
    void getAllEvents() {
        when(eventRepository.findAll()).thenReturn(List.of(event));
        assertThat(eventService.getAllEvents()).usingRecursiveComparison()
                .ignoringFields("createdAt").isEqualTo(List.of(eventDTO));
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