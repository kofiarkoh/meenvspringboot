package com.softport.meenvspringboot.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MiscData {
    private float pricePerSMS = 0.04f;
    private String appVersion = "4.23";
}
