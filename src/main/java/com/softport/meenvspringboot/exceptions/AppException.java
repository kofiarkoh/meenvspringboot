package com.softport.meenvspringboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



public class AppException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private HttpStatus status;

    public AppException(String message,HttpStatus status){
        super(message);
        System.out.println("appexception");
        this.status = status;

    }

    public HttpStatus getStatus() {
        return status;
    }
}
