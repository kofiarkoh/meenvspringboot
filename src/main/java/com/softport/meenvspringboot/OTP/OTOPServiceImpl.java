package com.softport.meenvspringboot.OTP;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTOPServiceImpl implements OTPService{

    private final OTPRepository otpRepository;

    @Override
    public boolean isOTPValid(OTP otp) {
        return false;
    }

    @Override
    public OTP generate(String value,String firstId, String secondId) {
        Random random = new Random();
        long otpCode = random.nextLong(99999);
        OTP otp = new OTP();
        otp.setCode(String.valueOf(otpCode));
        otp.setDate(new Date());
        otp.setFirstIdentifier(firstId);
        otp.setSecondIdentifier(secondId);

        return otpRepository.save(otp);
    }
}
