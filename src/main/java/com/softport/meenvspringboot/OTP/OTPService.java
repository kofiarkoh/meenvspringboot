package com.softport.meenvspringboot.OTP;

import java.util.Date;

public interface OTPService {

    boolean isOTPValid(OTP otp);

    OTP generate(String firstId, String secondId);

    OTP getOTP(String code);

    boolean isOTPValid(Date date);
}
