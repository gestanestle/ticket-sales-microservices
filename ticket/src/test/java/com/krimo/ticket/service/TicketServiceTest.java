package com.krimo.ticket.service;

import com.krimo.ticket.dao.EventDAO;
import com.krimo.ticket.data.Outbox;
import com.krimo.ticket.data.Section;
import com.krimo.ticket.data.Ticket;
import com.krimo.ticket.data.TicketTest;
import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.exception.ApiRequestException;
import com.krimo.ticket.repository.OutboxRepository;
import com.krimo.ticket.repository.TicketDetailsRepository;
import com.krimo.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock private TicketRepository ticketRepository;
    @Mock private TicketDetailsRepository ticketDetailsRepository;
    @Mock private OutboxRepository outboxRepository;
    @Mock private EventDAO eventDAO;

    @InjectMocks
    @Autowired
    private TicketServiceImpl ticketService;

    @Captor
    private ArgumentCaptor<Ticket> ticketCaptor;
    @Captor
    private ArgumentCaptor<Outbox> outboxCaptor;
    TicketDTO ticketDTO;

    Ticket ticket;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(
                ticketRepository, ticketDetailsRepository,
                outboxRepository, eventDAO);

        ticketDTO = TicketTest.ticketDTOInit();
        ticket = TicketTest.ticketInit();
    }

    @Test
    void purchaseTicketButEventCanceled() {
        String errMsg = "Event has been canceled.";

        when(eventDAO.isCanceled(anyLong())).thenReturn(true);
        assertThatThrownBy( () -> ticketService.purchaseTicket(ticketDTO))
                .isInstanceOf(ApiRequestException.class).hasMessageContaining(errMsg);
    }

    @Test
    void purchaseTicketButSoldOut() {
        String errMsg = "Sold out.";

        int sold = 10;
        int stock = 5;

        when(eventDAO.isCanceled(anyLong())).thenReturn(false);
        when(ticketRepository.getSold(anyLong(), any(Section.class))).thenReturn(sold);
        when(ticketDetailsRepository.getStock(anyLong(), any(Section.class))).thenReturn(stock);

        assertThatThrownBy(() -> ticketService.purchaseTicket(ticketDTO))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining(errMsg);
    }

    @Test
    void purchaseTicketOK() {
        int sold = 5;
        int stock = 10;
        String eventName = "Event 1";

        // passing the negative blocks
        when(eventDAO.isCanceled(anyLong())).thenReturn(false);
        when(ticketRepository.getSold(anyLong(), any(Section.class))).thenReturn(sold);
        when(ticketDetailsRepository.getStock(anyLong(), any(Section.class))).thenReturn(stock);

        // saving to ticket table
        when(ticketRepository.saveAndFlush(ticketCaptor.capture())).thenReturn(ticket);
        when(eventDAO.getEventName(anyLong())).thenReturn(Optional.of(eventName));

        ticketService.purchaseTicket(ticketDTO);

        verify(ticketRepository).saveAndFlush(ticketCaptor.capture());
        assertThat(ticketCaptor.getValue()).usingRecursiveComparison()
                .ignoringFields("id", "ticketCode","purchasedAt").isEqualTo(ticket);

        // saving to outbox table
        verify(outboxRepository).save(outboxCaptor.capture());

    }

    @Test
    void viewTicket() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(ticket));
        assertThat(ticketService.viewTicket(anyLong()))
                .usingRecursiveComparison()
                .ignoringFields("id", "purchasedAt")
                .isEqualTo(ticketDTO);
    }

    @Test
    void cancelPurchase() {
        ticketService.cancelPurchase(anyLong());
        verify(ticketRepository).deleteById(anyLong());
    }








}