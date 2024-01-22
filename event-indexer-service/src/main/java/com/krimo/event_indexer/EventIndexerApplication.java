package com.krimo.event_indexer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class EventIndexerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventIndexerApplication.class, args);
    }

}
