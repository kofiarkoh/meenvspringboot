package com.softport.meenvspringboot.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Group {

    private final Long id;

    private final String name;

    private final Long contacts;

}
