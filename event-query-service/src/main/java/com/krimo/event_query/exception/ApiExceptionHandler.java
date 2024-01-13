package com.krimo.event_query.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleRequestException (ApiRequestException e) {

        ApiException apiException = new ApiException(e.getMessage(), e.getStatus(),
                ZonedDateTime.now(ZoneId.of("Asia/Manila")));

        return new ResponseEntity<>(apiException, e.getStatus());
    }

}
