package com.krimo.event_command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ResponseObject {

    private String message;
    private HttpStatus status;
    private Object data;
    private ZonedDateTime timestamp;

    public static ResponseObject of(String message, HttpStatus status, Object data) {
        return new ResponseObject(message, status, data, ZonedDateTime.now(ZoneId.of("Asia/Manila")));
    }
}
