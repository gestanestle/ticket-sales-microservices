package com.krimo.ticket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krimo.ticket.data.MockData;
import com.krimo.ticket.dto.broker_msg.EventInbox;
import com.krimo.ticket.dto.broker_msg.EventInboxPayload;
import com.krimo.ticket.models.Event;
import com.krimo.ticket.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private ObjectMapper mapper;
    @InjectMocks
    @Autowired
    private EventService eventService;

    @BeforeEach
    void setUp() {

        eventService = new EventService(eventRepository, mapper);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void persistEvent() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        EventInboxPayload inboxPayload = new EventInboxPayload(1L, false);
        String payload = objectMapper.writeValueAsString(inboxPayload);
        EventInbox inbox = new EventInbox(null, "event_status", payload, LocalDateTime.now());

        Event event = MockData.eventInit();
        when(mapper.readValue(anyString(), eq(EventInbox.class))).thenReturn(inbox);
        when(mapper.readValue(anyString(), eq(EventInboxPayload.class))).thenReturn(inboxPayload);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.getReferenceById(1L)).thenReturn(event);

        eventService.persistEvent(objectMapper.writeValueAsString(inbox));
        verify(eventRepository, times(1)).save(any(Event.class));

    }
}