package com.softport.meenvspringboot.topup;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MomoDTO {

    private final String phone;
    private final String provider;
}
