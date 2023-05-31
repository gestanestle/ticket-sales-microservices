package com.krimo.event.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic eventNewTopic() {
        return TopicBuilder.name("event.maxCapacity").build();
    }

    @Bean
    public NewTopic eventUpdatesTopic() {
        return TopicBuilder.name("event.update").build();
    }
}
