package com.softport.meenvspringboot.services;

import com.softport.meenvspringboot.dto.SendMessageDTO;
import com.softport.meenvspringboot.group.Contacts;
import com.softport.meenvspringboot.messages.Message;

import java.util.Collection;
import java.util.List;

public interface MessageService {
    List<Message> getMessageByUserId(Long userId);


    void saveMessage(Long userId, Collection<Contacts> recipients, SendMessageDTO sendMessageDTO);
}
