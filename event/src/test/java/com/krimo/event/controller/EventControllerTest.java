package com.krimo.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krimo.event.data.Event;
import com.krimo.event.data.EventTest;
import com.krimo.event.dto.EventDTO;
import com.krimo.event.exception.ApiRequestException;
import com.krimo.event.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EventController.class)
class EventControllerTest {

    @MockBean
    private EventService eventService;
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    String eventDtoJson;
    Event event;
    EventDTO eventDTO;

    @BeforeEach
    void setUp() {
        eventDTO = EventTest.eventDTOInit();

        try {
            objectMapper.registerModule(new JavaTimeModule());
            eventDtoJson = objectMapper.writeValueAsString(eventDTO);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Unable to serialize message.");
        }

        event = EventTest.eventInit();

    }

    @Test
    void createEvent() throws Exception {
        when(eventService.createEvent(any(EventDTO.class))).thenReturn(1L);

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
    void getEvent() throws Exception {
        when(eventService.getEvent(anyLong())).thenReturn(eventDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("http://localhost:8081/api/v2/events/%s", 1)))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(eventDTO.getName()));
    }

    @Test
    void getAllEvents() throws Exception {
        when(eventService.getAllEvents()).thenReturn(List.of(eventDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8081/api/v2/events"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(eventDTO.getName()));
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