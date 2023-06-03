
package com.krimo.ticket.repository;

import com.krimo.ticket.TicketMgmtApplication;
import com.krimo.ticket.config.PostgresContainerEnv;
import com.krimo.ticket.data.TestEntityBuilder;
import com.krimo.ticket.data.Ticket;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles({"ticketTest"})
@ExtendWith({SpringExtension.class})
@SpringBootTest(
        classes = {TicketMgmtApplication.class},
        webEnvironment = WebEnvironment.RANDOM_PORT
)
class TicketRepositoryTest extends PostgresContainerEnv {
    @Autowired
    private TicketRepository ticketRepository;

    TicketRepositoryTest() {
    }

    @Test
    void findByEventCode() {
        TestEntityBuilder testEntityBuilder = new TestEntityBuilder();
        Ticket ticket = testEntityBuilder.ticket();
        ticketRepository.save(ticket);
        boolean expected = ticketRepository.getEmails(ticket.getEventCode()).isEmpty();
        AssertionsForClassTypes.assertThat(expected).isFalse();
    }
}