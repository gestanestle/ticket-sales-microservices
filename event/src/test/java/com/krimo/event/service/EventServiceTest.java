package com.krimo.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.event.data.Event;
import com.krimo.event.data.Section;
import com.krimo.event.data.TestEntityBuilder;
import com.krimo.event.dto.EventDTO;
import com.krimo.event.exception.ApiRequestException;
import com.krimo.event.repository.EventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    @Autowired
    private EventService eventService;

    TestEntityBuilder testEntityBuilder = new TestEntityBuilder();
    HashMap<Section, Integer> registeredAttendees = new HashMap<>();

    @Value("${event.topic.name}")
    String topic;

    Event event;
    EventDTO eventDTO;
    String eventCode;

    @BeforeEach
    void setUp() {

        eventService = new EventService(kafkaTemplate, eventRepository, objectMapper);

        event = testEntityBuilder.event();
        eventDTO = testEntityBuilder.eventDTO();

        eventCode = "d21d68f9-667c-42c0-8a0a-7bc01db1a45a";
        event.setEventCode(eventCode);

    }

    @AfterEach
    void tearDown() {
        event = null;
    }

    void whenFindEvent() {
        when(eventRepository.findByEventCode(eventCode)).thenReturn(Optional.of(event));
    }

    void captureEvent() {
        ArgumentCaptor<Event> argumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).usingRecursiveComparison().ignoringFields( "eventCode", "dateTime").isEqualTo(event);
    }

    @Test
    void createEvent() {
        eventService.createEvent(eventDTO);
        captureEvent();
    }

    @Test
    void readEvent() {
        whenFindEvent();
        assertThat(eventService.readEvent(eventCode)).usingRecursiveComparison().isEqualTo(event);
    }

    @Test
    void readAllEvents() {
        assertThat(eventService.readAllEvents()).isInstanceOf(List.class);
    }

    @Test
    void updateEvent() {
        // When
        whenFindEvent();
        eventService.updateEvent(eventCode, eventDTO);

        // Then
        verify(eventRepository, atLeast(1)).findByEventCode(eventCode);
        captureEvent();

        // When
        String eventJson = null;

        try {
            eventJson  = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Then
        verify(kafkaTemplate, atLeast(1)).send(topic, eventJson);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate).send(argumentCaptor.capture(), argumentCaptor.capture());

        List<String> capturedValues = argumentCaptor.getAllValues();
        assertEquals(capturedValues.get(0), topic);
        assertEquals(capturedValues.get(1), eventJson);
    }

    @Test
    void addAttendeeMaxFull() {
        // Given
        registeredAttendees.putAll(event.getMaxCapacity());
        event.setRegisteredAttendees(registeredAttendees);

        // When
        whenFindEvent();

        // Then
        assertThatThrownBy( ()-> eventService.addAttendee(eventCode, Section.VIP))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("Section is already full.");

        verify(eventRepository, atLeast(1)).findByEventCode(eventCode);

    }

    @Test
    void addAttendee() {
        // Given
        registeredAttendees.put(Section.VIP, 1);

        // When
        whenFindEvent();

        // Then
        eventService.addAttendee(eventCode, Section.VIP);
        assertEquals(event.getRegisteredAttendees(), registeredAttendees);
    }

    @Test
    void deleteEvent() {
        // Given
        event.setId(2L);

        // When
        whenFindEvent();
        eventService.deleteEvent(eventCode);

        // THen
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(eventRepository).deleteById(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue(), 2L);
    }
}