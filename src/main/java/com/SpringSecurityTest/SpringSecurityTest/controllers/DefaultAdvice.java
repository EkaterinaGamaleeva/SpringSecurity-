package com.SpringSecurityTest.SpringSecurityTest.controllers;


import com.SpringSecurityTest.SpringSecurityTest.response.ConsumerErrorResponse;
import com.SpringSecurityTest.SpringSecurityTest.response.ConsumerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return new ResponseEntity<>(ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ConsumerErrorResponse> handlerException(ConsumerNotFoundException e) {
        ConsumerErrorResponse response = new ConsumerErrorResponse(e.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
