package com.softport.meenvspringboot.dto;

import java.util.List;

import com.softport.meenvspringboot.messages.MessageRecipient;

import lombok.Data;

@Data
public class SendOneTimeMessageDTO {

    private String message;
    private String senderId;
    private List<MessageRecipient> recipients;

}
