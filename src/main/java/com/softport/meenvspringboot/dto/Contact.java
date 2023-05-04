package com.softport.meenvspringboot.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Contact {

    private final String name;
    private final String phoneNumber;
}
