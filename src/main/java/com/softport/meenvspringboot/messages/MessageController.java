package com.softport.meenvspringboot.messages;

import com.softport.meenvspringboot.dto.SendOneTimeMessageDTO;
import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.group.Group;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.uellosend.UelloSend;
import com.softport.meenvspringboot.user.User;
import com.softport.meenvspringboot.repositories.GroupRepository;
import com.softport.meenvspringboot.repositories.MessageRepository;
import com.softport.meenvspringboot.services.AuthenticationService;
import com.softport.meenvspringboot.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(path = "/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;

    private final MessageService messageService;
    private final UserRepository userRepository;
    private final MessageRecipientRepository messageRecipientRepository;

    @GetMapping
    public ResponseEntity<?> getUserMessages() {
        User user = AuthenticationService.getAuthenticatedUser();

        return new ResponseEntity<>(messageService.getMessageByUserId(user.getId()), HttpStatus.OK);
    }

    @PostMapping("one-time")
    public ResponseEntity<?> storeMessage(@RequestBody @Valid SendOneTimeMessageDTO data) {

        User user = AuthenticationService.getAuthenticatedUser();
        long recipientCount = data.getRecipients().size();

        if (recipientCount > user.getSmsBalance()) {
            throw new AppException("You have insufficient balance", HttpStatus.BAD_REQUEST);
        }
        Message message = new Message();
        message.setMessage(data.getMessage());
        message.setSenderId(data.getSenderId());
        data.getRecipients().forEach(recipient -> recipient.setMessage(message));
        messageRepository.save(message);
        messageRecipientRepository.saveAll(data.getRecipients());

        // simulate SMS sending here.

        //  update user balance
        user.setSmsBalance(user.getSmsBalance() - recipientCount);
        user.setSmsSent(user.getSmsSent() + recipientCount);
        userRepository.save(user);
        return new ResponseEntity<>("Message recieved for delivery.", HttpStatus.OK);

        /*  if (sendMessageDTO.isToGroup()) {
            Group group = groupRepository.findById(sendMessageDTO.getGroupId())
                    .orElseThrow(() -> new AppException("Group not found", HttpStatus.NOT_FOUND));
        
            this.messageService.saveMessage(user.getId(), group.getContacts(), sendMessageDTO);
        
            recipientCount = group.getContacts().size();
            if (user.getSmsBalance() < recipientCount) {
                throw new AppException(
                        "Insufficient SMS balance", HttpStatus.BAD_REQUEST);
            } */
        /*  UelloSend.sendCampaign(sendMessageDTO.getMessage(),
                sendMessageDTO.getSenderId(),
                group.getContacts().stream().map(c -> c.getPhoneNumber()).collect(Collectors.toList()));
        */
        // } else {

        // handle one-time recipients;
        // validate phone numbers if messages is not sent to a group;
        /*  if ((sendMessageDTO.getRecipients() == null) || sendMessageDTO.getRecipients().isEmpty()) {
            throw new AppException("Message recipients not found ", HttpStatus.BAD_REQUEST);
        }
        
        // recipients is guaranteed not to be empty so check for 10 digit phone number
        if (!sendMessageDTO.getRecipients().stream().allMatch(r -> r.getPhoneNumber().length() == 10)) {
            throw new AppException("Recipient phone number must be 10 digits.", HttpStatus.BAD_REQUEST);
        }
        
        // save individual message to database.
        this.messageService.saveMessage(user.getId(), sendMessageDTO.getRecipients(), sendMessageDTO);
        recipientCount = sendMessageDTO.getRecipients().size();
        if (user.getSmsBalance() < recipientCount) {
            throw new AppException(
                    "Insufficient SMS balance", HttpStatus.BAD_REQUEST);
        } */
        /*  UelloSend.sendCampaign(sendMessageDTO.getMessage(),
                sendMessageDTO.getSenderId(),
                sendMessageDTO.getRecipients().stream().map(c -> c.getPhoneNumber()).collect(Collectors.toList()));
        */
        //  }

        //  update user balance
        /*  user.setSmsBalance(user.getSmsBalance() - recipientCount);
        user.setSmsSent(user.getSmsSent() + recipientCount);
        userRepository.save(user);
        return new ResponseEntity<>("Message recieved for delivery.", HttpStatus.OK); */

    }

    /*
    * FOLLOWING END POINTS ARE FOR ADMIN DASHBOARD ONLY
    * */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getMessageByUserId(@PathVariable("userId") long userId) {
        return new ResponseEntity<>(messageService.getMessageByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllMessages() {
        return new ResponseEntity<>(messageRepository.getMessageByAllUsers(), HttpStatus.OK);
    }

}
