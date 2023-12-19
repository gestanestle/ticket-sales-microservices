package com.krimo.event_command.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krimo.event_command.EventCommandApplication;
import com.krimo.event_command.config.BaseContainerEnv;
import com.krimo.event_command.data.Event;
import com.krimo.event_command.data.EventFactory;
import com.krimo.event_command.dto.EventDTO;
import com.krimo.event_command.exception.ApiRequestException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

@SpringBootTest(classes = EventCommandApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest extends BaseContainerEnv {

    @LocalServerPort Integer port;
    ObjectMapper objectMapper = new ObjectMapper();

    String eventDtoJson;
    Event event;
    EventDTO eventDTO;

    @BeforeEach
    void setUp() {

        RestAssured.baseURI = "http://localhost:" + port;


        eventDTO = EventFactory.eventDTOInit();

        try {
            objectMapper.registerModule(new JavaTimeModule());
            eventDtoJson = objectMapper.writeValueAsString(eventDTO);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException(HttpStatus.SERVICE_UNAVAILABLE, "Unable to serialize message.");
        }

        event = EventFactory.eventInit();

    }

    @Test
    void createEvent() {
        given().contentType(ContentType.JSON).body(eventDtoJson)
                .when().post("/api/v2/events")
                .then().statusCode(201);
    }
}
