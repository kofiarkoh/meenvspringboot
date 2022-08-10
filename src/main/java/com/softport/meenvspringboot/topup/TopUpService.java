package com.softport.meenvspringboot.topup;

public interface TopUpService {

     boolean isOTPInValid(TopUp topUp);

     void verifyPaymentWebhookResponse(ChargeResult chargeResult);
}
