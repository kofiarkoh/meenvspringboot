package com.softport.meenvspringboot.topup;

import lombok.Data;
import lombok.RequiredArgsConstructor;


//@RequiredArgsConstructor
@Data
public class InitTransactionResponse {
    private  boolean status;
    private  String message;
   private  InitTransactionData data;
}

@Data
class InitTransactionData{
    private  String authorization_url;
    private  String access_code;
    private String reference;
}