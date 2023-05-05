package com.softport.meenvspringboot.dto;

import java.util.List;

import com.softport.meenvspringboot.group.Contact;
import com.softport.meenvspringboot.messages.Message;

import lombok.Data;

@Data
public class SendMessageDTO extends Message {
    private List<Contact> recipients;
    private long groupId;
}
