package com.ioh.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleLedgerNotFoundException(NotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
                request.getDescription(false), String.valueOf(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public final ResponseEntity<Object> handleLedgerAlreadyExistsException(AlreadyExistsException ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
                request.getDescription(false), String.valueOf(HttpStatus.ALREADY_REPORTED.value()));
        return new ResponseEntity(exceptionResponse, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(BadClientDataException.class)
    public final ResponseEntity<Object> handleLedgerBadClientDataException(BadClientDataException ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
                request.getDescription(false), String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
