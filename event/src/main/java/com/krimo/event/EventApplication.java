package com.krimo.event;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@EnableEurekaClient
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Event API", version = "1.0"))
public class EventApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventApplication.class, args);
    }
}