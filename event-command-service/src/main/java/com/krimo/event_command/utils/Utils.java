package com.krimo.event_command.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.event_command.data.types.EventOutboxPayload;
import com.krimo.event_command.exception.ApiRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String writeToString(EventOutboxPayload eventOutboxPayload) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(eventOutboxPayload);
        }catch (JsonProcessingException e) {
            throw new ApiRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "Event Command Service: Error serializing to outbox data.");
        }

        return payload;
    }
}
