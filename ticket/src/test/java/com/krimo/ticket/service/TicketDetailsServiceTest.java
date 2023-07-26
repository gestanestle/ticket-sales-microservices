package com.krimo.ticket.service;


import com.krimo.ticket.data.Section;
import com.krimo.ticket.data.TicketDetails;
import com.krimo.ticket.data.TicketDetailsPK;
import com.krimo.ticket.data.TicketDetailsTest;
import com.krimo.ticket.dto.TicketDetailsDTO;
import com.krimo.ticket.exception.ApiRequestException;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketDetailsServiceTest {

    @Mock
    private TicketDetailsRepository ticketDetailsRepository;
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    @Autowired
    private TicketDetailsServiceImpl ticketDetailsService;

    @Captor
    private ArgumentCaptor<TicketDetails> captor;

    Long eventId;
    String section;
    TicketDetails ticketDetails;
    TicketDetailsDTO ticketDetailsDTO;

    @BeforeEach
    void setUp() {
        ticketDetailsService = new TicketDetailsServiceImpl(ticketDetailsRepository, ticketRepository);

        eventId = 1L;
        section = "VIP";
        ticketDetails = TicketDetailsTest.ticketDetailsVIP();
        ticketDetailsDTO = TicketDetailsTest.ticketDetailsDTOInit();
    }

    @Test
    void setNewTicketDetails() {
        when(ticketDetailsRepository.findById(any(TicketDetailsPK.class))).thenReturn(Optional.empty());

        ticketDetailsService.setTicketDetails(eventId, ticketDetailsDTO);
        verify(ticketDetailsRepository).save(captor.capture());
        assertThat(captor.getValue())
                .usingRecursiveComparison()
                .ignoringFields("pk.event")
                .isEqualTo(ticketDetails);
    }

    @Test
    void updateExistingTicketDetails() {
        when(ticketDetailsRepository.findById(any(TicketDetailsPK.class))).thenReturn(Optional.of(ticketDetails));

        when(ticketRepository.getSold(anyLong(), any(Section.class))).thenReturn(0);
        ticketDetailsDTO.setPrice(9999);

        ticketDetailsService.setTicketDetails(eventId, ticketDetailsDTO);
        verify(ticketDetailsRepository).save(captor.capture());
        assertThat(captor.getValue().getPrice())
                .isEqualTo(ticketDetailsDTO.getPrice());
    }

    @Test
    void failedUpdateBecauseTicketsSold() {
        String errMsg = "Invalid request. Tickets have already been sold.";

        when(ticketDetailsRepository.findById(any(TicketDetailsPK.class))).thenReturn(Optional.of(ticketDetails));

        when(ticketRepository.getSold(anyLong(), any(Section.class))).thenReturn(1);
        assertThatThrownBy(() -> ticketDetailsService.setTicketDetails(eventId, ticketDetailsDTO))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining(errMsg);
    }

    @Test
    void getTicketDetailsByEvent() {
        when(ticketDetailsRepository.getTicketDetailsByEvent(anyLong()))
                .thenReturn(List.of(ticketDetails));


        List<TicketDetailsDTO> actual = ticketDetailsService.getTicketDetailsByEvent(eventId);
        assertThat(actual.get(0))
                .usingRecursiveComparison()
                .isEqualTo(ticketDetailsDTO);
    }


}
