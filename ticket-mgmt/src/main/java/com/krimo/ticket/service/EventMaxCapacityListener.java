package com.krimo.ticket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.ticket.data.EventCapacityMessage;
import com.krimo.ticket.data.SectionAttendeeMap;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

public interface EventMaxCapacityListener {

    void listenToEventCapacity(String message);
}

@Service
@RequiredArgsConstructor
class EventMaxCapacityListenerImpl implements EventMaxCapacityListener {

    private static final Logger logger = LoggerFactory.getLogger(EventMaxCapacityListenerImpl.class);
    private final ObjectMapper objectMapper;
    private final RedisService redisService;

    @KafkaListener(topics = "event.maxCapacity", groupId = "event-capacity-consumer-group")
    @Override
    public void listenToEventCapacity(String message) {
        EventCapacityMessage eventCapacityMessage = new EventCapacityMessage();
        try {
            eventCapacityMessage = objectMapper.readValue(message, EventCapacityMessage.class);
        } catch (JsonProcessingException e) {
            logger.debug("Failed to deserialize event capacity message from kafka [event.maxCapacity].");
        }

        SectionAttendeeMap sectionAttendeeMap = new SectionAttendeeMap();
        sectionAttendeeMap.setMaxCapacity(eventCapacityMessage.getMaxCapacity());
        redisService.storeSectionAttendeeMap(eventCapacityMessage.getEventCode(), sectionAttendeeMap);

    }

}
