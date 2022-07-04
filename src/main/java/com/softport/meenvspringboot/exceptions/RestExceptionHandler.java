package com.softport.meenvspringboot.exceptions;

import javax.servlet.http.HttpServletRequest;

import com.softport.meenvspringboot.dto.ErrorDTO;
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

import java.util.Date;


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
        // TODO Auto-generated method stub
        return new ResponseEntity<>(ex.getMessage(), null, HttpStatus.BAD_REQUEST);
        // return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }

}
