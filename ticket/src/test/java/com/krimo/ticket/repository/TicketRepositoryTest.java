package com.krimo.ticket.repository;

import com.krimo.ticket.TicketApplication;
import com.krimo.ticket.config.PostgresContainerEnv;
import com.krimo.ticket.data.TicketDetails;
import com.krimo.ticket.data.TicketDetailsTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TicketApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketRepositoryTest extends PostgresContainerEnv {

    @Autowired
    private TicketDetailsRepository ticketDetailsRepository;

    TicketDetails expected;

    @BeforeEach
    void setUp() {
        expected =TicketDetailsTest.ticketDetailsVIP();
        ticketDetailsRepository.save(expected);
    }

    @Test
    void getTicketDetailsByEvent() {

        List<TicketDetails> actual = ticketDetailsRepository
                .getTicketDetailsByEvent(expected.getPk().getEventId());

        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(expected);
    }

}
