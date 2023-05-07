package com.softport.meenvspringboot.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MessageDTO {

    public String senderId;

    public String message;

    public long recipientsCount;

    public Date date;

    public String getDate() {
        return date.toString();
    }

}
