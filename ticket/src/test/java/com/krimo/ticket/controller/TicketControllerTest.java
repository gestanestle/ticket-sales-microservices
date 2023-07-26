package com.krimo.ticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.krimo.ticket.data.TicketTest;
import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.exception.ApiRequestException;
import com.krimo.ticket.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TicketController.class)
class TicketControllerTest {

    @MockBean
    private TicketService ticketService;
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    TicketDTO ticketDTO;
    String dtoJson;

    @BeforeEach
    void setUp() {
        ticketDTO = TicketTest.ticketDTOInit();

        try {
            objectMapper.registerModule(new JavaTimeModule());
            dtoJson = objectMapper.writeValueAsString(ticketDTO);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Unable to serialize message.");
        }
    }

    @Test
    void purchaseTicket() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8082/api/v2/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is(201));
    }

    @Test
    void viewTicket() throws Exception {
        when(ticketService.viewTicket(anyLong())).thenReturn(ticketDTO);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("http://localhost:8082/api/v2/tickets/%s", 1)))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.event_id").value(ticketDTO.getEventId()))
                .andExpect(jsonPath("$.ticket_code").value(ticketDTO.getTicketCode()));
    }

    @Test
    void cancelPurchase() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(String.format("http://localhost:8082/api/v2/tickets/%s", 1)))
                .andExpect(status().is(200));
    }
}