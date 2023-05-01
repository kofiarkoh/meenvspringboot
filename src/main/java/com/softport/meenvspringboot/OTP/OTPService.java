package com.softport.meenvspringboot.OTP;

import java.util.Date;

import com.softport.meenvspringboot.user.User;

public interface OTPService {

    boolean isOTPValid(OTP otp);

    OTP generate(String firstId, String secondId, User user);

    OTP getOTP(String code);

    boolean isOTPValid(Date date);

    void deleteOTP(OTP otp);

}
