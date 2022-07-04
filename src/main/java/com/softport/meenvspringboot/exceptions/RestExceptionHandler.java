package com.softport.meenvspringboot.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /*
     * @ExceptionHandler(ConstraintViolationException.class)
     * public ResponseEntity<?> hanl(ConstraintViolationException exception,
     * HttpServletRequest request) {
     * System.out.println("helow");
     * String errorMessage = exception.getCause().getMessage();
     * log.error(exception.getMessage());
     * return new ResponseEntity<>(errorMessage, null, HttpStatus.BAD_REQUEST);
     * }
     */

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        // TODO Auto-generated method stub
        return new ResponseEntity<>(ex.getMessage(), null, HttpStatus.BAD_REQUEST);
        // return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }

}
