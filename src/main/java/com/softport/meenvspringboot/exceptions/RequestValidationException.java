package com.softport.meenvspringboot.exceptions;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.softport.meenvspringboot.dto.ErrorDTO;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDTO.setDate(new Date().toGMTString());

        return new ResponseEntity<>(errorDTO, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException appException, HttpServletRequest request) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(appException.getMessage());
        errorDTO.setStatus(appException.getStatus().value());
        errorDTO.setDate(new Date().toGMTString());

        return new ResponseEntity<>(errorDTO, null, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String messages = "Failed to convert value of " + ex.getValue() + " to type "
                + ex.getRequiredType().getSimpleName();
        return new ResponseEntity<>(new ErrorDTO(messages, HttpStatus.BAD_REQUEST.value(), new Date().toGMTString()),
                null, HttpStatus.BAD_REQUEST);
    }
}
