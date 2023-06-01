package com.krimo.ticket.repository;

import com.krimo.ticket.TicketMgmtApplication;
import com.krimo.ticket.config.PostgresContainerEnv;
import com.krimo.ticket.data.TestEntityBuilder;
import com.krimo.ticket.data.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("ticketTest")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TicketMgmtApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketRepositoryTest extends PostgresContainerEnv {

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void findByEventCode() {
        // Given
        TestEntityBuilder testEntityBuilder = new TestEntityBuilder();
        Ticket ticket = testEntityBuilder.ticket();

        // When
        ticketRepository.save(ticket);

        // Then
        boolean expected = ticketRepository.findByEventCode(ticket.getEventCode()).isEmpty();
        assertThat(expected).isFalse();

    }
}