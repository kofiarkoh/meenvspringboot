package com.softport.meenvspringboot.uellosend;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
public class UelloSend {

    public static UelloSendResponse sendCampaign(String message, String senderId, List<String> recipients){
        CampaignRequest campaignRequest = new CampaignRequest();
        campaignRequest.setMessage(message);
        campaignRequest.setRecipient(recipients);
        campaignRequest.setSender_id(senderId);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("content-type","application/json");

        HttpEntity<CampaignRequest> httpEntity = new HttpEntity<>(campaignRequest,httpHeaders);
        UelloSendResponse response = restTemplate.postForObject(
                "https://uellosend.uvitechgh.com/sms/api/campaign/",
                httpEntity,
                UelloSendResponse.class

        );

        log.info("uello is {}",response);
        return  response;

    }
}

@Data()
class CampaignRequest{
    private String api_key = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.=eyJ1c2VyX2lkIjo2NDEsImFwaVNlY3JldCI6IkZQdWVVenhxSTBMT0JZOCIsImV4cCI6MjAyMDEwfQ";
    private String sender_id;
    private String message;
    private List<String> recipient;

}

