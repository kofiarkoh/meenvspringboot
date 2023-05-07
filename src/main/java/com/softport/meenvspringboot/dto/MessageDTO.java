package com.softport.meenvspringboot.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageDTO {

    public String senderId;

    public String message;

    public long recipientsCount;

    public Date date;

    public String getDate() {
        return date.toString();
    }

}
