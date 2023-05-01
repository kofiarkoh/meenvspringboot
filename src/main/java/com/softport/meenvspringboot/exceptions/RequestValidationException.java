package com.softport.meenvspringboot.exceptions;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RequestValidationException extends ResponseEntityExceptionHandler {

    String regex = "([a-z])([A-Z]+)";
    String replacement = "$1_$2";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, Object> responseBody = new LinkedHashMap<>();

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .stream()
                .forEach(x -> {
                    errors.put(x.getField().replaceAll(regex, replacement).toLowerCase(), x.getDefaultMessage());
                });

        responseBody.put("errors", errors);
        responseBody.put("timestamp", new Date());
        responseBody.put("status", status.value());

        return new ResponseEntity<>(responseBody, headers, status);
    }
}
