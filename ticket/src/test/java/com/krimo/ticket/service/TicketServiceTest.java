package com.krimo.ticket.service;

import com.krimo.ticket.client.EventClient;
import com.krimo.ticket.data.Event;
import com.krimo.ticket.data.TestEntityBuilder;
import com.krimo.ticket.repository.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private EventClient eventClient;
    @InjectMocks
    @Autowired
    private TicketService ticketService;

    TestEntityBuilder testEntityBuilder = new TestEntityBuilder();
    Ticket ticket;
    Event event;

    @BeforeEach
    void setUp() {
        ticketService = new TicketService(ticketRepository, eventClient);
        ticket = testEntityBuilder.ticket();
        event = new Event(ticket.getEventCode(), Arrays.asList(ticket, ticket));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void buyTicket() {
        // Given
        CustomerDTO customerDTO = testEntityBuilder.customerDTO();

        when(ticketRepository.findByEventCode(customerDTO.getEventCode())).thenReturn(Optional.of(event));

        //When
        ticketService.buyTicket(customerDTO);

        // Then
        ArgumentCaptor<Event> argumentCaptor =  ArgumentCaptor.forClass(Event.class);
        verify(ticketRepository).save(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue(), event);

        assertThat(ticketService.buyTicket(customerDTO))
                .usingRecursiveComparison()
                .ignoringFields("dateTime", "ticket.purchaseDateTime", "ticket.ticketCode")
                .isEqualTo(new ReturnObject(ticket));
    }

    @Test
    void allEvents() {
        ticketService.allEvents();
        EventList eventList = new EventList(Arrays.asList(event, event));
        when(ticketRepository.findAll().stream().toList()).thenReturn(Arrays.asList(event, event));
        assertThat(ticketService.allEvents()).usingRecursiveComparison().isEqualTo(eventList);
    }

    @Test
    void eventTickets() {
        TicketList ticketList = new TicketList(Arrays.asList(ticket, ticket));
        when(ticketRepository.findByEventCode(event.getEventCode())).thenReturn(Optional.of(event));
        assertThat(ticketService.eventTickets(event.getEventCode())).usingRecursiveComparison().isEqualTo(ticketList);

    }
}