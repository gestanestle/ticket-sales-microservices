package com.krimo.email.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.email.dto.AttendeeEmailAddressesMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public interface AttendeesEmailConsumer {
    List<String> fetchAttendeesEmailAddresses(String eventCode);
}

@Service
@RequiredArgsConstructor
class AttendeesEmailConsumerImpl implements AttendeesEmailConsumer {


    private final ObjectMapper objectMapper;
    private final Properties kafkaProperties;

    @Override
    public List<String> fetchAttendeesEmailAddresses(String eventCode) {

        List<String> emailsList = new ArrayList<>();

        AttendeeEmailAddressesMessage message;

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kafkaProperties)){
            consumer.subscribe(Collections.singletonList(eventCode));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    String value = record.value();
                    message = objectMapper.readValue(value, AttendeeEmailAddressesMessage.class);
                    if (message.getEventCode().equals(eventCode)) {
                        emailsList.add(message.getEmailAddress());
                    }
                }

                if (records.isEmpty()) {  break; }
            }

        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }

        return emailsList;
    }
}
