package com.krimo.ticket.repository;

import com.krimo.ticket.TicketApplication;
import com.krimo.ticket.config.MongoContainerEnv;
import com.krimo.ticket.data.Event;
import com.krimo.ticket.data.TestEntityBuilder;
import com.krimo.ticket.data.Ticket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TicketApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketRepositoryTest extends MongoContainerEnv {

    @Autowired
    private TicketRepository ticketRepository;

    TestEntityBuilder testEntityBuilder = new TestEntityBuilder();

    @Test
    void findByEventCode() {
        // Given

        Ticket ticket = testEntityBuilder.ticket();

        Event event = new Event(ticket.getEventCode(), Collections.singletonList(ticket));

        // When
        ticketRepository.save(event);

        // Then
        boolean expected = ticketRepository.findByEventCode(ticket.getEventCode()).isPresent();
        assertThat(expected).isTrue();
    }
}