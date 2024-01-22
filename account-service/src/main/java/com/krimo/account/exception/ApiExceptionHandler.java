package com.krimo.account.exception;

import com.krimo.account.dto.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleRequestException (ApiRequestException e) {
        return new ResponseEntity<>(ResponseObject.of(e.getMessage(), e.getStatus().value(), null), e.getStatus());
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchElement (NoSuchElementException e) {
        return new ResponseEntity<>(ResponseObject.of("Record not found.", HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND);
    }

}
