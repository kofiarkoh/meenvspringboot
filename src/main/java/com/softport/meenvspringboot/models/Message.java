package com.softport.meenvspringboot.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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


}
