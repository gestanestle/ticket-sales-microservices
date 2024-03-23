package com.krimo.ticket.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krimo.ticket.dto.PurchaseEvent;
import com.krimo.ticket.exception.ApiRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@Slf4j
public class Utils {

    private static final int CODE_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public static String generateSerialCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            stringBuilder.append(CHARACTERS.charAt(SECURE_RANDOM.nextInt(CHARACTERS.length())));
        }
        return stringBuilder.toString();
    }

    public static String writeToJson(PurchaseEvent input) {
        String res;
        try {
            res = OBJECT_MAPPER.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new ApiRequestException(HttpStatus.SERVICE_UNAVAILABLE, "Server cannot perform request at the moment.");
        }
        return res;
    }

    private Utils() {}

}
