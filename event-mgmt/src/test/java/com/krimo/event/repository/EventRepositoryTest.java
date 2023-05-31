package com.krimo.event.repository;

import com.krimo.event.EventMgmtApplication;
import com.krimo.event.data.TestEntityBuilder;
import com.krimo.event.config.PostgresContainerEnv;
import com.krimo.event.data.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EventMgmtApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventRepositoryTest extends PostgresContainerEnv {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void findByEventCode() {

        TestEntityBuilder testEntityBuilder = new TestEntityBuilder();

        // Given
        String eventCode = UUID.randomUUID().toString();
        Event event = testEntityBuilder.event();
        event.setEventCode(eventCode);

        // When
        eventRepository.save(event);

        // Then
        boolean expected = eventRepository.findByEventCode(eventCode).isPresent();
        assertThat(expected).isTrue();
    }
}