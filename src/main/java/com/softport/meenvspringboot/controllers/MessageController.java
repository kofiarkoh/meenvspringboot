package com.softport.meenvspringboot.controllers;

import com.softport.meenvspringboot.dto.SendMessageDTO;
import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @Slf4j
@RequestMapping(path= "/messages")
public class MessageController {

    @GetMapping
    public ResponseEntity<?> getUserMessages(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> storeMessage(@RequestBody SendMessageDTO sendMessageDTO){

        // validate phone numbers if messages is not sent to a group;
        if (sendMessageDTO.isToGroup()){

        }else {
            // handle one-time recipients;
            if((sendMessageDTO.getRecipients() == null) || sendMessageDTO.getRecipients().isEmpty()   ){
                throw new AppException("Message recipients not found ",HttpStatus.BAD_REQUEST);
            }
            // recipients is guaranteed not to be empty so check for 10 digit phone number
            if (! sendMessageDTO.getRecipients().stream().allMatch(r-> r.getPhoneNumber().length() == 10) ){
                throw new AppException("Recipient phone number must be 10 digits.",HttpStatus.BAD_REQUEST);
            }

        }

        return new ResponseEntity<>(sendMessageDTO, HttpStatus.OK);
    }
}
