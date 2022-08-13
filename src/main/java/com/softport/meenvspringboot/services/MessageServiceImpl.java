package com.softport.meenvspringboot.services;

import com.softport.meenvspringboot.dto.SendMessageDTO;
import com.softport.meenvspringboot.group.Contacts;
import com.softport.meenvspringboot.messages.Message;
import com.softport.meenvspringboot.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private final MessageRepository messageRepository;

    @Override
    public List<Message> getMessageByUserId(Long userId) {
        return this.messageRepository.getMessageByUserId(userId);
    }

    @Override
    public void saveMessage(Long userId ,Collection<Contacts> recipients, SendMessageDTO sendMessageDTO) {
        String messageId = String.format("%06d",new Random().nextInt(999999));
        List<Message> messageList = recipients.stream().map(recipient->{
            Message message = new Message();
            message.setMessage(sendMessageDTO.getMessage());
            message.setRecipient(recipient.getPhoneNumber());
            message.setToGroup(true);
            message.setStatus("pending");
            message.setSenderId(sendMessageDTO.getSenderId());
            message.setUserId(userId);
            message.setRecipientCount(recipients.size());
            message.setMessageId(messageId);
            return  message;
        }).collect(Collectors.toList());
        messageRepository.saveAll(messageList);
    }


}
