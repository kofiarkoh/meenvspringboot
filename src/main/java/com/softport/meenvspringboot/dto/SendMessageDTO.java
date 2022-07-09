package com.softport.meenvspringboot.dto;

import com.softport.meenvspringboot.models.Contacts;
import com.softport.meenvspringboot.models.Message;
import lombok.Data;

import java.util.Collection;

@Data
public class SendMessageDTO extends Message {
    private Collection<Contacts> recipients;
}
