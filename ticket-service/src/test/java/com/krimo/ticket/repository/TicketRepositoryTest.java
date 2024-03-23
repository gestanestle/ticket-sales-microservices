package com.krimo.ticket.repository;

import com.krimo.ticket.config.BaseContainerEnv;
import com.krimo.ticket.data.MockData;
import com.krimo.ticket.models.Event;
import com.krimo.ticket.models.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketRepositoryTest extends BaseContainerEnv {

    @Autowired private EventRepository eventRepository;
    @Autowired private TicketRepository ticketRepository;

    @Test
    void shouldGetByEvent() {
        Event event = MockData.eventInit();
        Ticket ticket = MockData.ticketInit();

        event.setTickets(Set.of(ticket));
        ticket.setTicketId(1L);

        Event e = eventRepository.save(event);

        assertThat(ticketRepository.getByEvent(e.getEventId()).get(0))
                .usingRecursiveComparison().ignoringFields("event"). isEqualTo(ticket);
    }
}
