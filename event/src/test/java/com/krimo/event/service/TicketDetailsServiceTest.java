package com.krimo.event.service;

import com.krimo.event.data.*;
import com.krimo.event.dto.TicketDetailsDTO;
import com.krimo.event.repository.EventRepository;
import com.krimo.event.repository.TicketDetailsRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketDetailsServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private TicketDetailsRepository ticketDetailsRepository;

    @InjectMocks
    @Autowired
    private TicketDetailsServiceImpl ticketDetailsService;

    @Captor
    private ArgumentCaptor<TicketDetails> captor;

    Long eventId;
    String section;
    Event event;
    TicketDetails ticketDetails;
    TicketDetailsDTO ticketDetailsDTO;

    @BeforeEach
    void setUp() {
        ticketDetailsService = new TicketDetailsServiceImpl(eventRepository, ticketDetailsRepository);

        eventId = 1L;
        section = "VIP";
        event = EventTest.eventInit();
        ticketDetails = TicketDetailsTest.ticketDetailsVIP();
        ticketDetailsDTO = TicketDetailsTest.ticketDetailsDTOInit();
    }

    @Test
    void setTicketDetails() {
        ticketDetailsService.setTicketDetails(eventId, ticketDetailsDTO);
        verify(ticketDetailsRepository).save(captor.capture());
        assertThat(captor.getValue())
                .usingRecursiveComparison()
                .ignoringFields("pk.event")
                .isEqualTo(ticketDetails);
    }

    @Test
    void getTicketDetailsByEvent() {
        when(eventRepository.getReferenceById(anyLong())).thenReturn(event);
        when(ticketDetailsRepository.getTicketDetailsByEvent(any(Event.class)))
                .thenReturn(List.of(ticketDetails));


        List<TicketDetailsDTO> actual = ticketDetailsService.getTicketDetailsByEvent(eventId);
        assertThat(actual.get(0))
                .usingRecursiveComparison()
                .isEqualTo(ticketDetailsDTO);
    }

    @Test
    void updateTicketDetails() {
        when(ticketDetailsRepository.getReferenceById(any(TicketDetailsPK.class)))
                .thenReturn(ticketDetails);

        ticketDetailsDTO.setPrice(9999);

        ticketDetailsService.updateTicketDetails(eventId, section, ticketDetailsDTO);
        verify(ticketDetailsRepository).save(captor.capture());
        assertThat(captor.getValue().getPrice())
                .usingRecursiveComparison()
                .isEqualTo(ticketDetailsDTO.getPrice());
    }

}
