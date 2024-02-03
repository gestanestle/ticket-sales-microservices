package com.krimo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ApiGatewayApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
