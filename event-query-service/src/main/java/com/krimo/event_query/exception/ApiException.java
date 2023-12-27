package com.krimo.event_query.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiException (
        String message,
        HttpStatus httpStatus,
        ZonedDateTime zonedDateTime
) {
}
