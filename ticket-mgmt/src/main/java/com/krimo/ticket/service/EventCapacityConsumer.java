package com.krimo.ticket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.ticket.data.EventCapacityMessage;
import com.krimo.ticket.data.Section;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

public interface EventCapacityConsumer {
    Integer fetchEventCapacity(String eventCode, Section section);
}

@Service
@RequiredArgsConstructor
class EventCapacityConsumerImpl implements EventCapacityConsumer{
    private static final Logger logger = LoggerFactory.getLogger(EventCapacityConsumerImpl.class);

    private final ConsumerFactory<String, String> consumerFactory;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "event.maxCapacity";

    @Override
    public Integer fetchEventCapacity(String eventCode, Section section) {
        int integer = 0;

        try (Consumer<String, String> consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(Collections.singletonList(TOPIC));
             while (true) {
                 ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(60));

                 for (ConsumerRecord<String, String> record : records) {
                     String value = record.value();
                     logger.info("RECORD: " + value);

                     EventCapacityMessage eventCapacity = objectMapper.readValue(value, EventCapacityMessage.class);

                     if (eventCapacity.getEventCode().equals(eventCode)) {
                         integer = eventCapacity.getMaxCapacity().get(section);
                     }
                 }
                 if (records.isEmpty()) { break; }
             }

        } catch (Exception e) {
            logger.debug("EXCEPTION THROWN BY FETCH EVENT CAPACITY" + e.getMessage());
        }

        return integer;
    }
}
