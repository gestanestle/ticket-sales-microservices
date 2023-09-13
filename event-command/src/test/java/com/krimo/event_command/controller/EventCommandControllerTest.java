package com.krimo.event_command.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krimo.event_command.data.Event;
import com.krimo.event_command.data.EventFactory;
import com.krimo.event_command.dto.EventDTO;
import com.krimo.event_command.exception.ApiRequestException;
import com.krimo.event_command.service.EventCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EventCommandController.class)
class EventCommandControllerTest {

    @MockBean
    private EventCommandService eventCommandService;
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    String eventDtoJson;
    Event event;
    EventDTO eventDTO;

    @BeforeEach
    void setUp() {
        eventDTO = EventFactory.eventDTOInit();

        try {
            objectMapper.registerModule(new JavaTimeModule());
            eventDtoJson = objectMapper.writeValueAsString(eventDTO);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException(HttpStatus.SERVICE_UNAVAILABLE, "Unable to serialize message.");
        }

        event = EventFactory.eventInit();

    }

    @Test
    void createEvent() throws Exception {
        when(eventCommandService.createEvent(any(EventDTO.class))).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8081/api/v2/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is(201))
                .andExpect(content().string("1"));
    }

    @Test
    void updateEvent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .put(String.format("http://localhost:8081/api/v2/events/%s", 1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is(200));
    }

}