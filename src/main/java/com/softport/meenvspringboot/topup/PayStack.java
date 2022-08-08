package com.softport.meenvspringboot.topup;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

public class PayStack {

    public static InitTransactionResponse makePaymentReques(MomoRequestDTO body){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer sk_test_873b19dc74e0adc4f52b79d1fdfa2299aa16fc8b");
        httpHeaders.add("content-type","application/json");

        HttpEntity<MomoRequestDTO> httpEntity = new HttpEntity<>(body,httpHeaders) ;

        return restTemplate.postForObject(
                "https://api.paystack.co/transaction/initialize",
                httpEntity, InitTransactionResponse.class
        );
    }
}
