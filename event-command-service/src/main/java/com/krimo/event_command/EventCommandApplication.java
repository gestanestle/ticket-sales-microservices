package com.krimo.event_command;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Event Command Service API", version = "3.0"))
public class EventCommandApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventCommandApplication.class, args);
    }
}