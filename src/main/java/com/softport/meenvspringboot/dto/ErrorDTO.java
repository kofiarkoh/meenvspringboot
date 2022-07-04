package com.softport.meenvspringboot.dto;

import lombok.Data;

@Data
public class ErrorDTO {
    private String message;
    private int status;
    private String date;
}
