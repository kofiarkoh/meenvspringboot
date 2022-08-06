package com.softport.meenvspringboot.controllers;

import com.github.javafaker.Faker;
import com.softport.meenvspringboot.messages.Message;
import com.softport.meenvspringboot.repositories.MessageRepository;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.topup.TopUp;
import com.softport.meenvspringboot.topup.TopupRepository;
import com.softport.meenvspringboot.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FakeDataController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final TopupRepository topupRepository;

    @GetMapping("fake")
    public ResponseEntity<?> generateFakeData(){
        List<User> users = new  ArrayList<>();
        Faker faker = new Faker();

        for (int i =0; i < 20; i++){
            User user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setEmail(user.getFirstName()+"@gmail.com");
            user.setSmsBalance(faker.number().numberBetween(0,300));
            user.setSmsSent(faker.number().numberBetween(0,300));
            user.setCreatedAt(faker.date().birthday());
            user.setPhoneNumber(faker.phoneNumber().subscriberNumber(10));
            user.setPassword(passwordEncoder.encode("msp1"));
            user.setId(faker.number().randomNumber());
            users.add(user);
        }
        userRepository.saveAll(users);
        List<User> createdUsers = (List<User>) userRepository.findAll();
        createdUsers.stream().forEach(user -> {
            randomMessages(user.getId(),faker);
            fakeTopUps(user.getId(),faker);
        });
        // generate random transaction data
        return new ResponseEntity<>("Fake data generated", HttpStatus.OK);
    }

    private void randomMessages(long userId,Faker faker){
        List<Message> msgs = new ArrayList<>();
        System.out.println("calling user");
        for (int i = 0 ; i < 10 ;i++){
            Message msg = new Message();
            msg.setMessage(faker.lorem().words(10).toString());
            msg.setUserId(userId);
            msg.setStatus("sent");
            msg.setSenderId(faker.lorem().word());
            msg.setToGroup(false);
            msg.setDate(faker.date().birthday());
            msg.setRecipient(faker.lorem().word());
            msg.setRecipientCount(faker.number().numberBetween(1,100));
            msg.setMessageId(faker.idNumber().valid());
          //  System.out.println("generating message");
            msgs.add(msg);
        }

        messageRepository.saveAll(msgs);
    }

    private void fakeTopUps(long userId,Faker faker){
        List<TopUp> topUps = new ArrayList<>();
        for (int i=0 ; i< 8; i++){
            TopUp topUp = new TopUp();
            topUp.setOtp(Long.parseLong(faker.number().digits(5)));
            topUp.setPhoneNumber(faker.phoneNumber().subscriberNumber(10));
            topUp.setAmount(faker.number().numberBetween(1,50));
            topUp.setSmsQuantity(faker.number().numberBetween(20,500));
            topUp.setDate(faker.date().birthday());
            topUp.setTransactionId(faker.idNumber().valid());
            topUp.setNetwork(faker.options().nextElement(List.of("MTN","AIRTIG","VOD")));
            topUp.setStatus(faker.options().nextElement(List.of("PENDING","FAILED","SUCCESS")));
            topUps.add(topUp);
        }
        topupRepository.saveAll(topUps);
    }
}
