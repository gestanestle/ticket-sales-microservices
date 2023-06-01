package com.krimo.ticket.service;

import com.krimo.ticket.data.TestEntityBuilder;
import com.krimo.ticket.data.Ticket;
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

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;
    @InjectMocks
    @Autowired
    private TicketServiceImpl ticketService;

    TestEntityBuilder testEntityBuilder = new TestEntityBuilder();

    Ticket ticket;
    String eventCode;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(ticketRepository);

        ticket = testEntityBuilder.ticket();
        eventCode = ticket.getEventCode();
    }

    @AfterEach
    void tearDown() {
        ticket = null;
        eventCode = null;
    }

    @Test
    void saveTicket() {
        ticketService.saveTicket(ticket);

        ArgumentCaptor<Ticket> argumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).usingRecursiveComparison().ignoringFields("purchaseDateTime").isEqualTo(ticket);
    }

    @Test
    void emailsList() {
        assertThat(ticketService.emailsList(eventCode)).isInstanceOf(List.class);
    }
}