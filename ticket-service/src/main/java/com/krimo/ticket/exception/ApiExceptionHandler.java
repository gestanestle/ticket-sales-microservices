package com.krimo.ticket.exception;

import com.krimo.ticket.dto.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleRequestException (ApiRequestException e) {
        return new ResponseEntity<>(ResponseBody.of(e.getMessage(), e.getStatus(), null), e.getStatus());
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchElement (NoSuchElementException e) {
        return new ResponseEntity<>(ResponseBody.of("Record not found.", HttpStatus.NOT_FOUND, null), HttpStatus.NOT_FOUND);
    }
}
