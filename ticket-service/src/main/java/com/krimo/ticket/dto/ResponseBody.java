package com.krimo.ticket.dto;

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
public class ResponseBody {

    private String message;
    private HttpStatus status;
    private Object data;
    private ZonedDateTime timestamp;

    public static ResponseBody of(String message, HttpStatus status, Object data) {
        return new ResponseBody(message, status, data, ZonedDateTime.now(ZoneId.of("Asia/Manila")));
    }
}
