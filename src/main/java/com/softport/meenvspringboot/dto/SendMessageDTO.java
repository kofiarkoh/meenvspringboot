package com.softport.meenvspringboot.dto;

import com.softport.meenvspringboot.group.Contacts;
import com.softport.meenvspringboot.messages.Message;
import lombok.Data;

import java.util.Collection;

@Data
public class SendMessageDTO extends Message {
    private Collection<Contacts> recipients;
    private long groupId;
}
