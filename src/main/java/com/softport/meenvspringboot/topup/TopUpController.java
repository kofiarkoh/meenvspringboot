package com.softport.meenvspringboot.topup;

import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TopUpController {

    private final TopupRepository topupRepository;
    @PostMapping("user/topup")
    public ResponseEntity<?> requestTopUp(@RequestBody TopUp topUp){

        //generate unique transaction ID and OTP
        Random random = new Random();
        topUp.setTransactionId(String.valueOf(random.nextLong(999999)));
        long otp = random.nextLong(99999);
        topUp.setOtp(otp);
        topUp.setDate(new Date());
        topUp.setUserId(AuthenticationService.getAuthenticatedUser().getId());

        return new ResponseEntity<>(topupRepository.save(topUp),HttpStatus.OK);

    }

    @GetMapping ("topup/verify_otp/{otp}")
    public ResponseEntity<?> verifyTopOTP(@PathVariable long otp){

        TopUp topupData = topupRepository.findByOtp(otp).orElseThrow(()->new AppException("Invalid OTP",HttpStatus.NOT_FOUND));

        /*
        * OTP is only valid for 5 mins
        * check if OTP is valid before procressing payment with Paystack.
        * */
       Instant now = Instant.now();
       Duration timeElapsed = Duration.between(topupData.getDate().toInstant(),now);
       if (timeElapsed.toMinutes() > 5) {
          throw  new AppException("Expired OTP",HttpStatus.BAD_REQUEST);
       }
       // Process payment with Paystack.
       return new ResponseEntity<>("Processing payment",HttpStatus.OK);
    }
}
