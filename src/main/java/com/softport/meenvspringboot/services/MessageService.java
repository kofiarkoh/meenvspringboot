package com.softport.meenvspringboot.services;

import com.softport.meenvspringboot.dto.SendOneTimeMessageDTO;
import com.softport.meenvspringboot.group.Contact;
import com.softport.meenvspringboot.messages.Message;

import java.util.Collection;
import java.util.List;

public interface MessageService {
    List<Message> getMessageByUserId(Long userId);

    void saveMessage(Long userId, Collection<Contact> recipients, SendOneTimeMessageDTO sendMessageDTO);
}
