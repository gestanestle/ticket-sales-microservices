package com.krimo.ticket.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.ticket.exception.ApiRequestException;
import com.krimo.ticket.payload.TicketPurchasePayload;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class Utils {

    private static final int CODE_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String generateSerialCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            stringBuilder.append(CHARACTERS.charAt(SECURE_RANDOM.nextInt(CHARACTERS.length())));
        }
        return stringBuilder.toString();
    }

    public static String writeToJson(TicketPurchasePayload input) {
        String res;
        try {
            res = OBJECT_MAPPER.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Unable to serialize message.");
        }
        return res;
    }

}
