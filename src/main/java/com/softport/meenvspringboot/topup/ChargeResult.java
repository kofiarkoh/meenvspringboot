package com.softport.meenvspringboot.topup;

import lombok.Data;

@Data
public class ChargeResult {
    private String event;
    private ChargeData data;
}

@Data
class ChargeData{
    private String reference;
    private String status;


}
