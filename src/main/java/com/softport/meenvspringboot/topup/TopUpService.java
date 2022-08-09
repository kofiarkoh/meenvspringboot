package com.softport.meenvspringboot.topup;

public interface TopUpService {

     boolean isOTPValid(TopUp topUp);

     void verifyPaymentWebhookResponse(ChargeResult chargeResult);
}
