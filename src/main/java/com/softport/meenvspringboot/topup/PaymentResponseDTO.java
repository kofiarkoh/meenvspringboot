package com.softport.meenvspringboot.topup;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PaymentResponseDTO {
    private final String event;
    private final PaymentResponse data;
}

class PaymentResponse{

}