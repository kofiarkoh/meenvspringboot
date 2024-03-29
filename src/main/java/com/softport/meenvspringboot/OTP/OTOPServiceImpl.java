package com.softport.meenvspringboot.OTP;


import com.softport.meenvspringboot.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
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
    public OTP generate(String firstId, String secondId) {
        Random random = new Random();
        long otpCode = random.nextLong(99999);
        OTP otp = new OTP();
        otp.setCode(String.valueOf(otpCode));
        otp.setDate(new Date());
        otp.setFirstIdentifier(firstId);
        otp.setSecondIdentifier(secondId);

        return otpRepository.save(otp);
    }

    @Override
    public OTP getOTP(String code) {

        OTP otp = otpRepository.findByCode(code);
        if (otp == null){
            throw new AppException(
                    "OTP not found",
                    HttpStatus.NOT_FOUND
            );

        }
        if (! isOTPValid(otp.getDate())){
            throw new AppException(
                    "OTP has expired.",
                    HttpStatus.BAD_REQUEST
            );
        }

        //verify otp
        return  otp;
    }

    @Override
    public boolean isOTPValid(Date date) {
        /*
         * OTP is only valid for 5 mins
         *
         * */
        Instant now = Instant.now();
        Duration timeElapsed = Duration.between(date.toInstant(),now);

        return  timeElapsed.toMinutes() < 5 ;
    }
}
