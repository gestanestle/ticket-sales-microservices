package com.krimo.ticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krimo.ticket.data.MockData;
import com.krimo.ticket.dto.TicketDTO;

import com.krimo.ticket.service.TicketService;
import lombok.extern.slf4j.Slf4j;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TicketController.class)
@Slf4j
class TicketControllerTest {

    @MockBean
    private TicketService ticketService;
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    TicketDTO dto;

    String dtoJson;

    @BeforeEach
    void setUp() {
        dto = MockData.ticketDTOInit();

        try {
            objectMapper.registerModule(new JavaTimeModule());
            dtoJson = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            log.error("Unable to serialize message.");
        }

    }

    @Test
    void createTicket() throws Exception {
        when(ticketService.createTicket(anyLong(), any(TicketDTO.class))).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(String.format("http://localhost:8082/api/v3/event/%s/tickets", 1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.message",  org.hamcrest.Matchers.is("Ticket successfully defined.")));

    }

    @Test
    void getEventTickets() throws Exception {
        when(ticketService.getTicketsByEvent(anyLong())).thenReturn(List.of(dto));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("http://localhost:8082/api/v3/event/%s/tickets",1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.data[0].ticketId",org.hamcrest.Matchers.is(1)))
                .andExpect(jsonPath("$.message",  org.hamcrest.Matchers.is("Event tickets successfully retrieved.")));
    }

    @Test
    void getTicket() throws Exception {
        when(ticketService.getTicket(anyLong())).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("http://localhost:8082/api/v3/tickets/%s",1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.data.ticketId",org.hamcrest.Matchers.is(1)))
                .andExpect(jsonPath("$.message",  org.hamcrest.Matchers.is("Ticket successfully retrieved.")));
    }

    @Test
    void updateTicket() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .put(String.format("http://localhost:8082/api/v3/tickets/%s", 1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.message",  org.hamcrest.Matchers.is("Ticket successfully updated.")));
    }

    @Test
    void deleteEvent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(String.format("http://localhost:8081/api/v3/tickets/%s", 1)))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.message",  org.hamcrest.Matchers.is("Ticket successfully deleted.")));
    }
}
