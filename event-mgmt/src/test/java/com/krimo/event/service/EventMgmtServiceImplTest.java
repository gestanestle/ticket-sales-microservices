package com.krimo.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.event.data.Event;
import com.krimo.event.data.TestEntityBuilder;
import com.krimo.event.dto.EventCapacityMessage;
import com.krimo.event.dto.EventDTO;
import com.krimo.event.exception.ApiRequestException;
import com.krimo.event.repository.EventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventMgmtServiceImplTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    @Autowired
    private EventMgmtServiceImpl eventService;

    TestEntityBuilder testEntityBuilder = new TestEntityBuilder();

    Event event;
    EventDTO eventDTO;
    String eventCode;

    @BeforeEach
    void setUp() {

        eventService = new EventMgmtServiceImpl(objectMapper, kafkaTemplate, eventRepository);

        event = testEntityBuilder.event();
        eventDTO = testEntityBuilder.eventDTO();

        eventCode = "FU9QJSB61E";
        event.setEventCode(eventCode);
    }

    @AfterEach
    void tearDown() {

        event = null;
        eventDTO = null;
        eventCode = null;

    }

    void whenFindEvent() {
        when(eventRepository.findByEventCode(eventCode)).thenReturn(Optional.of(event));
    }

    // verify that the Event entity in service parameter
    // is the same as the one being saved by eventRepository
    void captureEvent() {
        ArgumentCaptor<Event> argumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).usingRecursiveComparison().ignoringFields( "eventCode", "dateTime").isEqualTo(event);
    }

    @Test
    void createEvent() {
        // I. TEST: If the event being saved matches the same as the parameter
        eventService.createEvent(eventDTO);
        captureEvent();

        // II. TEST: The message serializer

        // GIVEN: Topic and message
        String expectedTopic = "event.maxCapacity";
        EventCapacityMessage messageObject = new EventCapacityMessage(eventCode, event.getMaxCapacity());

        String expectedMessage;
        try {
            expectedMessage = objectMapper.writeValueAsString(messageObject);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Unable to serialize message.");
        }

        // WHEN THE TRY-CATCH BLOCK DOESN'T THROW SUCH EXCEPTION

        // (1) Then verify that kafka's sending of message is actually being invoked
        verify(kafkaTemplate, atLeast(1)).send(expectedTopic, expectedMessage);

        // (2) Then verify that the topic and message being sent byme
        // kafka template is the same as the given topic and message
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate).send(argumentCaptor.capture(), argumentCaptor.capture());

        List<String> capturedValues = argumentCaptor.getAllValues();
        assertEquals(capturedValues.get(0), expectedTopic);
        assertEquals(capturedValues.get(1), expectedMessage);
    }


    @Test
    void readEvent() {
        whenFindEvent();
        // TEST: If the event being returned by this method is the same as the given event
        assertThat(eventService.readEvent(eventCode)).usingRecursiveComparison().isEqualTo(event);
    }

    @Test
    void readAllEvents() {
        assertThat(eventService.readAllEvents()).isInstanceOf(List.class);
    }

    @Test
    void updateEvent() throws JsonProcessingException{
        // When
        whenFindEvent();
        eventService.updateEvent(eventCode, eventDTO);

        // I. TEST: findByEventCode is being invoked
        verify(eventRepository, atLeast(1)).findByEventCode(eventCode);

        // II. TEST: If the event saving matches the parameter
        captureEvent();


        // III. TEST: The message serializer

        // GIVEN: Topic and message
        String expectedTopic = "event.update";
        eventDTO.setEventCode(eventCode);

        String expectedMessage;
        try {
            expectedMessage = objectMapper.writeValueAsString(eventDTO);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Unable to serialize message.");
        }

        // WHEN THE TRY-CATCH BLOCK DOESN'T THROW SUCH EXCEPTION

        // (1) Then verify that kafka's sending of message is actually being invoked
        verify(kafkaTemplate, atLeast(1)).send(expectedTopic, expectedMessage);

        // (2) Then verify that the topic and message being sent by
        // kafka template is the same as the given topic and message
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate).send(argumentCaptor.capture(), argumentCaptor.capture());

        List<String> capturedValues = argumentCaptor.getAllValues();
        assertEquals(capturedValues.get(0), expectedTopic);
        assertEquals(capturedValues.get(1), expectedMessage);
    }

    @Test
    void deleteEvent() {
        // Given
        event.setId(2L);

        // When
        whenFindEvent();
        eventService.deleteEvent(eventCode);

        // THen
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(eventRepository).deleteById(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue(), 2L);
    }
}