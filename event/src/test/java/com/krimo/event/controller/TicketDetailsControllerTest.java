package com.krimo.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krimo.event.data.TicketDetails;
import com.krimo.event.data.TicketDetailsTest;
import com.krimo.event.dto.TicketDetailsDTO;
import com.krimo.event.exception.ApiRequestException;
import com.krimo.event.service.TicketDetailsService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = TicketDetailsController.class)
public class TicketDetailsControllerTest {

    @MockBean
    private TicketDetailsService ticketDetailsService;
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    String dtoJson;
    TicketDetailsDTO dto;
    TicketDetails ticketDetails;

    @BeforeEach
    void setUp() {
        dto = TicketDetailsTest.ticketDetailsDTOInit();

        try {
            objectMapper.registerModule(new JavaTimeModule());
            dtoJson = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Unable to serialize message.");
        }

        ticketDetails = TicketDetailsTest.ticketDetailsVIP();
    }

    @Test
    void setTicketDetails() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                    .post(String.format("http://localhost:8081/api/v2/events/%s/ticket-details", 1))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dtoJson)
                    .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is(201));

    }

    @Test
    void getTicketDetailsByEvent() throws Exception{
        when(ticketDetailsService.getTicketDetailsByEvent(anyLong())).thenReturn(List.of(dto));

        mockMvc.perform(MockMvcRequestBuilders
                    .get(String.format("http://localhost:8081/api/v2/events/%s/ticket-details", 1)))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].event_id").value(dto.getEventId()))
                .andExpect(jsonPath("$[0].section").value(dto.getSection().name()));
    }

    @Test
    void updateTicketDetails() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .put(String.format("http://localhost:8081/api/v2/events/%1$s/ticket-details/%2$s", 1, "vip"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is(200));
    }


}
