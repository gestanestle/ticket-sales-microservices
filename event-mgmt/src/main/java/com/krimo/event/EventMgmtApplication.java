package com.krimo.event;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Event Management API", version = "2.0"))
public class EventMgmtApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventMgmtApplication.class, args);
    }
}