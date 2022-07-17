package com.softport.meenvspringboot.messages;

import com.softport.meenvspringboot.dto.SendMessageDTO;
import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.group.Groups;
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

@RestController
@Slf4j
@RequestMapping(path = "/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<?> getUserMessages() {
        User user = AuthenticationService.getAuthenticatedUser();

        return new ResponseEntity<>(messageService.getMessageByUserId(user.getId()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> storeMessage(@RequestBody SendMessageDTO sendMessageDTO) {

        User user = AuthenticationService.getAuthenticatedUser();
        if (sendMessageDTO.isToGroup()) {
            Groups group = groupRepository.findById(sendMessageDTO.getGroupId()).orElseThrow(() -> new AppException("Group not found", HttpStatus.NOT_FOUND));

            this.messageService.saveMessage(user.getId(), group.getContacts(), sendMessageDTO);

        } else {

            // handle one-time recipients;
            // validate phone numbers if messages is not sent to a group;
            if ((sendMessageDTO.getRecipients() == null) || sendMessageDTO.getRecipients().isEmpty()) {
                throw new AppException("Message recipients not found ", HttpStatus.BAD_REQUEST);
            }

            // recipients is guaranteed not to be empty so check for 10 digit phone number
            if (!sendMessageDTO.getRecipients().stream().allMatch(r -> r.getPhoneNumber().length() == 10)) {
                throw new AppException("Recipient phone number must be 10 digits.", HttpStatus.BAD_REQUEST);
            }

            // save individual message to database.
            this.messageService.saveMessage(user.getId(), sendMessageDTO.getRecipients(), sendMessageDTO);
        }

        return new ResponseEntity<>("Message recieved for delivery.", HttpStatus.OK);
    }
}
