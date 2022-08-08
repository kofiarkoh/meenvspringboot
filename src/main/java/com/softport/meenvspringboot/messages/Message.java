package com.softport.meenvspringboot.messages;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity @Data @RequiredArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String senderId;

    private String message;

    private long userId;

    private String recipient;

    private boolean toGroup;

    private String status;

    private int recipientCount;

    private String messageId;

    private Date date = new Date();

    public String getDate(){
        return  this.date.toGMTString();
    }


}
