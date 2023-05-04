package com.softport.meenvspringboot.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

//@NoArgsConstructor()
@RequiredArgsConstructor
@Data
public class Group {

    private final Long id;

    private final String name;

    private final Long contacts;

    private List<Contact> contactList = new ArrayList<>(0);

}
