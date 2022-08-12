package com.softport.meenvspringboot.OTP;

public interface OTPService {

    boolean isOTPValid(OTP otp);

    OTP generate(String value,String firstId, String secondId);
}
