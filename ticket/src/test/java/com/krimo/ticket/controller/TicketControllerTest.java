package com.krimo.ticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krimo.ticket.data.Event;
import com.krimo.ticket.data.TestEntityBuilder;
import com.krimo.ticket.service.TicketService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TicketController.class)
class TicketControllerTest {

    @MockBean
    private TicketService ticketService;
    @Autowired
    private MockMvc mockMvc;

    TestEntityBuilder testEntityBuilder = new TestEntityBuilder();
    ObjectMapper objectMapper = new ObjectMapper();
    ReturnObject returnObject = new ReturnObject();
    Ticket ticket;
    CustomerDTO customerDTO;
    String customerDtoJson;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        ticket = testEntityBuilder.ticket();

        customerDTO = testEntityBuilder.customerDTO();

        try {
            customerDtoJson = objectMapper.writeValueAsString(customerDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        returnObject = null;
    }

    @Test
    void buyTicketSuccess() throws Exception {

        returnObject.setTicket(ticket);

        when(ticketService.buyTicket(any(CustomerDTO.class))).thenReturn(returnObject);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:9000/api/v1/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerDtoJson))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ticketCode").value(ticket.getTicketCode()));
    }

    @Test
    void buyTicketError() throws Exception{

        returnObject.setErrorMsg("Error message.");

        when(ticketService.buyTicket(any(CustomerDTO.class))).thenReturn(returnObject);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:9000/api/v1/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerDtoJson))
                .andExpect(status().is(400))
                .andExpect(content().string("Error message."));

    }

    @Test
    void allEvents() throws Exception{

        EventList eventList = new EventList(
                List.of(new Event(ticket.getEventCode(),
                        Collections.singletonList(ticket))));

        when(ticketService.allEvents()).thenReturn(eventList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:9000/api/v1/ticket/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void eventTickets() throws Exception{

        TicketList ticketList = new TicketList(Collections.singletonList(ticket));

        when(ticketService.eventTickets(ticket.getEventCode())).thenReturn(ticketList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("http://localhost:9000/api/v1/ticket/%s", ticket.getEventCode()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
}