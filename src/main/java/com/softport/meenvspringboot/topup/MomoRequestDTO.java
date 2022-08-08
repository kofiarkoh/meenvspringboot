package com.softport.meenvspringboot.topup;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MomoRequestDTO {
    private final float amount;
    private final String email;
    private final String currency;
    private final MomoDTO mobile_money;
    private final String callback_url;
    private final String reference;
}

