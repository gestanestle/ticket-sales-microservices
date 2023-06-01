package com.krimo.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krimo.event.data.Event;
import com.krimo.event.data.TestEntityBuilder;
import com.krimo.event.dto.EventDTO;
import com.krimo.event.exception.ApiRequestException;
import com.krimo.event.service.EventMgmtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EventMgmtController.class)
class EventMgmtControllerTest {

    @MockBean
    private EventMgmtService eventMgmtService;
    @Autowired
    private MockMvc mockMvc;

    TestEntityBuilder testEntityBuilder = new TestEntityBuilder();

    ObjectMapper objectMapper = new ObjectMapper();

    String eventDtoJson;
    String eventCodeOne;
    String eventCodeTwo;
    Event eventOne;
    Event eventTwo;
    EventDTO eventDTO;

    @BeforeEach
    void setUp() {
        eventCodeOne = "NXEMFO2QEA";
        eventCodeTwo = "0P6NY9LFR6";

        eventOne = testEntityBuilder.event();
        eventOne.setEventCode(eventCodeOne);

        eventTwo = testEntityBuilder.event();
        eventTwo.setEventCode(eventCodeTwo);

        eventDTO =  testEntityBuilder.eventDTO();

        try {
            objectMapper.registerModule(new JavaTimeModule());
            eventDtoJson = objectMapper.writeValueAsString(eventDTO);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Unable to serialize message.");
        }
    }

    @AfterEach
    void tearDown() {
        eventCodeOne = null;
        eventCodeTwo = null;
        eventOne = null;
        eventTwo = null;
        eventDTO = null;
        eventDtoJson = null;
    }

    @Test
    void createEvent() throws Exception {

        when(eventMgmtService.createEvent(eventDTO)).thenReturn(eventCodeOne);
        System.out.println(eventMgmtService.createEvent(eventDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:9000/api/v2/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is(201));
    }

    @Test
    void readEvent() throws Exception {
        when(eventMgmtService.readEvent(eventCodeOne)).thenReturn(eventOne);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("http://localhost:9000/api/v2/event/%s", eventCodeOne)))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.eventCode").value(eventCodeOne));
    }

    @Test
    void readAllEvents() throws Exception {
        when(eventMgmtService.readAllEvents()).thenReturn(Arrays.asList(eventOne, eventTwo));

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:9000/api/v2/event/all"))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].eventCode").value(eventCodeOne))
                .andExpect(jsonPath("$[1].eventCode").value(eventCodeTwo));
    }

    @Test
    void updateEvent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put(String.format("http://localhost:9000/api/v2/event/%s", eventCodeOne))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is(200));
    }

    @Test
    void deleteEvent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(String.format("http://localhost:9000/api/v2/event/%s", eventCodeOne)))
                .andExpect(status().is(200));
    }

}