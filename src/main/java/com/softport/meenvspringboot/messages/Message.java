package com.softport.meenvspringboot.messages;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity @Data @RequiredArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String senderId;

    @Lob
    @Column(length = 281)
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
