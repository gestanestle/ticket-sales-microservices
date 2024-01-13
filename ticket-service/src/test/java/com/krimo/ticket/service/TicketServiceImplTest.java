package com.krimo.ticket.service;

import com.krimo.ticket.data.MockData;
import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.exception.ApiRequestException;
import com.krimo.ticket.models.Event;
import com.krimo.ticket.models.Ticket;
import com.krimo.ticket.repository.EventRepository;
import com.krimo.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    @Autowired
    private TicketServiceImpl ticketService;
    @Captor
    private ArgumentCaptor<Ticket> ticketCaptor;

    Event event = MockData.eventInit();
    Ticket ticket = MockData.ticketInit();
    TicketDTO dto = MockData.ticketDTOInit();

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(ticketRepository, eventRepository);
    }

    @Test
    void createTicket() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(ticketRepository.saveAndFlush(ticketCaptor.capture())).thenReturn(ticket);
        ticketService.createTicket(dto.ticketId(), dto);
        assertThat(ticketCaptor.getValue()).usingRecursiveComparison()
                .ignoringFields("ticketId") .isEqualTo(ticket);
        verify(eventRepository, times(1)).findById(anyLong());
        verify(ticketRepository, times(1)).saveAndFlush(any(Ticket.class));
    }

    @Test
    void getTicket() {
        event.setTickets(Set.of(ticket));
        ticket.setTicketId(1L);
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(ticket));
        assertThat(ticketService.getTicket(1L)).isEqualTo(dto);
    }

    @Test
    void getTicketsByEvent() {
        event.setTickets(Set.of(ticket));
        ticket.setTicketId(1L);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(ticketRepository.getByEvent(any(Event.class))).thenReturn(List.of(ticket));
        assertThat(ticketService.getTicketsByEvent(1L)).isEqualTo(List.of(dto));
    }

    @Test
    void shouldUpdateType() {
        TicketDTO dtoU = MockData.ticketDTU1();
        when(ticketRepository.getReferenceById(1L)).thenReturn(ticket);
        ticketService.updateTicket(1L, dtoU);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void shouldThrowErrorOnUpdatePrice() {
        TicketDTO dtoU = MockData.ticketDTU2();
        ticket.setQtySold(499);
        when(ticketRepository.getReferenceById(1L)).thenReturn(ticket);
        assertThatThrownBy( () -> ticketService.updateTicket(1L, dtoU))
                .isInstanceOf(ApiRequestException.class).hasMessageContaining("Cannot update ticket price. Tickets have already been sold.");
    }

    @Test
    void shouldThrowErrorOnUpdateStock() {
        TicketDTO dtoU = MockData.ticketDTU3();
        ticket.setQtySold(499);
        when(ticketRepository.getReferenceById(1L)).thenReturn(ticket);
        assertThatThrownBy( () -> ticketService.updateTicket(1L, dtoU))
                .isInstanceOf(ApiRequestException.class).hasMessageContaining( "New quantity stock cannot be less than the current amount of ticket sold.");
    }

    @Test
    void deleteTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.ofNullable(ticket));
        ticketService.deleteTicket(1L);
        verify(ticketRepository, times(1)).deleteById(anyLong());
    }
}