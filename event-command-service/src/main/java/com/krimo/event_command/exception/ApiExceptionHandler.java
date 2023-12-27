package com.krimo.event_command.exception;

import com.krimo.event_command.dto.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleRequestException (ApiRequestException e) {
        return new ResponseEntity<>(ResponseObject.of(e.getMessage(), e.getStatus(), null), e.getStatus());
    }

}
