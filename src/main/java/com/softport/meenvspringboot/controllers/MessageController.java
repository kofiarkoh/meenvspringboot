package com.softport.meenvspringboot.controllers;

import com.softport.meenvspringboot.dto.SendMessageDTO;
import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.models.Groups;
import com.softport.meenvspringboot.models.Message;
import com.softport.meenvspringboot.models.User;
import com.softport.meenvspringboot.repositories.GroupRepository;
import com.softport.meenvspringboot.repositories.MessageRepository;
import com.softport.meenvspringboot.services.AuthenticationService;
import com.softport.meenvspringboot.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController @Slf4j
@RequestMapping(path= "/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;

    private final MessageService messageService;
    @GetMapping
    public ResponseEntity<?> getUserMessages(){
        User user = AuthenticationService.getAuthenticatedUser();

        return new ResponseEntity<>( messageService.getMessageByUserId(user.getId()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> storeMessage(@RequestBody SendMessageDTO sendMessageDTO){

        User user = AuthenticationService.getAuthenticatedUser();

        if (sendMessageDTO.isToGroup()){
            Groups group = groupRepository.findById(sendMessageDTO.getGroupId())
                    .orElseThrow(() ->new AppException("Group not found",HttpStatus.NOT_FOUND));
            List<Message> messageList = group.getContacts().stream().map(recipient->{
                Message message = new Message();
                message.setMessage(sendMessageDTO.getMessage());
                message.setRecipient(recipient.getPhoneNumber());
                message.setToGroup(true);
                message.setStatus("pending");
                message.setSenderId(sendMessageDTO.getSenderId());
                message.setUserId(user.getId());
                message.setRecipientCount(group.getContacts().size());
                return  message;
            }).collect(Collectors.toList());
            messageRepository.saveAll(messageList);
        }else {
            // handle one-time recipients;
            // validate phone numbers if messages is not sent to a group;
            if((sendMessageDTO.getRecipients() == null) || sendMessageDTO.getRecipients().isEmpty()   ){
                throw new AppException("Message recipients not found ",HttpStatus.BAD_REQUEST);
            }
            // recipients is guaranteed not to be empty so check for 10 digit phone number
            if (! sendMessageDTO.getRecipients().stream().allMatch(r-> r.getPhoneNumber().length() == 10) ){
                throw new AppException("Recipient phone number must be 10 digits.",HttpStatus.BAD_REQUEST);
            }


            // save individual message to database.
            List<Message> messageList = sendMessageDTO.getRecipients().stream().map(recipient->{
                Message message = new Message();
                message.setMessage(sendMessageDTO.getMessage());
                message.setRecipient(recipient.getPhoneNumber());
                message.setToGroup(false);
                message.setStatus("pending");
                message.setSenderId(sendMessageDTO.getSenderId());
                message.setUserId(user.getId());
                message.setRecipientCount(sendMessageDTO.getRecipients().size());
                return  message;
            }).collect(Collectors.toList());
            messageRepository.saveAll(messageList);


        }

        return new ResponseEntity<>( "Message recieved for delivery.", HttpStatus.OK);
    }
}
