package com.krimo.event_command;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Event Command API", version = "2.1"))
public class EventCommandApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventCommandApplication.class, args);
    }
}