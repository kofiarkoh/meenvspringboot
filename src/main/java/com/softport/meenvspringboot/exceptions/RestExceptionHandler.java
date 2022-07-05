package com.softport.meenvspringboot.exceptions;

import javax.servlet.http.HttpServletRequest;

import com.softport.meenvspringboot.dto.ErrorDTO;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Objects;


@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(AppException.class)
    public  ResponseEntity<?> handleAppException(AppException appException, HttpServletRequest request){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(appException.getMessage());
        errorDTO.setStatus(appException.getStatus().value());
        errorDTO.setDate(new Date().toGMTString());

        return  new ResponseEntity<>(errorDTO,null,HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        //respond with only first argument error.

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ex.getBindingResult().getFieldError().getField());
        stringBuilder.append(": ");
        stringBuilder.append(ex.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity<>( new ErrorDTO(stringBuilder.toString(), HttpStatus.BAD_REQUEST.value(), new Date().toGMTString()), null, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String messages  =  "Failed to convert value of "+ ex.getValue()  +" to type "+  ex.getRequiredType().getSimpleName() ;
        return  new ResponseEntity<>(new ErrorDTO(messages, HttpStatus.BAD_REQUEST.value(), new Date().toGMTString()),null,HttpStatus.BAD_REQUEST);
    }
}
