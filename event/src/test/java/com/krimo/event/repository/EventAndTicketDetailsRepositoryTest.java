package com.krimo.event.repository;

import com.krimo.event.EventApplication;
import com.krimo.event.config.PostgresContainerEnv;
import com.krimo.event.data.Event;
import com.krimo.event.data.EventTest;
import com.krimo.event.data.TicketDetails;
import com.krimo.event.data.TicketDetailsTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EventApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventAndTicketDetailsRepositoryTest extends PostgresContainerEnv {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketDetailsRepository ticketDetailsRepository;

    @Test
    void getTicketDetailsByEvent() {
        Event event = EventTest.eventInit();
        eventRepository.save(event);

        List<TicketDetails> expected = List.of(TicketDetailsTest.ticketDetailsVIP(), TicketDetailsTest.ticketDetailsUB());
        ticketDetailsRepository.saveAll(expected);

        List<TicketDetails> actual = ticketDetailsRepository.getTicketDetailsByEvent(event);

        assertThat(actual.size()).isEqualTo(expected.size());
    }
}
