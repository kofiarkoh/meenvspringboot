package com.softport.meenvspringboot.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.RequiredArgsConstructor;

//@NoArgsConstructor()
@RequiredArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Group {

    private final Long id;

    private final String name;

    private final Long contacts;

    private List<Contact> contactList = new ArrayList<>(0);

}
