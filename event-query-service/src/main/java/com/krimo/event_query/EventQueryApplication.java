package com.krimo.event_query;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@OpenAPIDefinition(info = @Info(title = "Event Query Service API", version = "3.0"))
public class EventQueryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventQueryApplication.class, args);
	}

}
