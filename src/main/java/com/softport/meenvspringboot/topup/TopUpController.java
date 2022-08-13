package com.softport.meenvspringboot.topup;

import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.services.AuthenticationService;
import com.softport.meenvspringboot.user.MiscData;
import com.softport.meenvspringboot.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    private final UserRepository userRepository;

    private final TopUpService topUpService;

    /*
    * request topup end point
    * */
    @PostMapping("user/topup")
    public ResponseEntity<?> requestTopUp(@RequestBody TopUp topUp){

        //generate unique transaction ID and OTP
        Random random = new Random();
        topUp.setTransactionId(String.valueOf(random.nextLong(999999)));
        long otp = random.nextLong(99999);
        Float amount = topUp.getSmsQuantity() * new MiscData().getPricePerSMS() ;
        topUp.setOtp(otp);
        topUp.setDate(new Date());
        topUp.setStatus("PENDING");
        topUp.setAmount(amount);
        topUp.setUserId(AuthenticationService.getAuthenticatedUser().getId());
        topupRepository.save(topUp);
        // Process payment with Paystack.
        MomoRequestDTO body = new MomoRequestDTO(
                String.format("%.2f",amount),
                "kofarkoh0@gmail.com",
                "GHS",
                new MomoDTO(topUp.getPhoneNumber(),topUp.getNetwork().toLowerCase()) ,
                "s", topUp.getTransactionId()
        );
        InitTransactionResponse initTransactionResponse = PayStack.makePaymentReques(body);

        return new ResponseEntity<>(
                initTransactionResponse.getData().getAuthorization_url()
                ,HttpStatus.OK);
    }

    /*
    * fetch all topups by current authenticated user
    * */

    @GetMapping("user/topups")
    public ResponseEntity<?> fetchAllTopUpsByAuthUser(){
        return new ResponseEntity<>(topupRepository.findAllByUserId(AuthenticationService.getAuthenticatedUser().getId()),HttpStatus.OK);
    }


    /*
    * veirfy OTP code
    * */
    @GetMapping ("top_up/verify_otp/{otp}")
    public ResponseEntity<?> verifyTopOTP(@PathVariable long otp){

        TopUp topupData = topupRepository.findByOtp(otp).orElseThrow(()->new AppException("Invalid OTP",HttpStatus.NOT_FOUND));
        // verify otp;
        log.info("OTP RECIEVED IS {}",otp);
        if ( topUpService.isOTPInValid(topupData)) {
             throw  new AppException("Expired OTP", HttpStatus.BAD_REQUEST);
        }

        // Process payment with Paystack.
        MomoRequestDTO body = new MomoRequestDTO(
                "100",
                "kofarkoh0@gmail.com",
                "GHS",
                new MomoDTO(topupData.getPhoneNumber(),topupData.getNetwork().toLowerCase()) ,
                "s", topupData.getTransactionId()
        );
        InitTransactionResponse initTransactionResponse = PayStack.makePaymentReques(body);

       return new ResponseEntity<>(initTransactionResponse.getData().getAuthorization_url(),HttpStatus.OK);
    }

    /*
    * momo web hook
    * */
    @PostMapping("trypay")
    public ResponseEntity<?> momoResponseHook() {
        String res = "Processing payment";

        //InitTransactionResponse s = PayStack.makePaymentReques();
        return new ResponseEntity(res,HttpStatus.OK);
    }


    @PostMapping("payment/hook")
    public ResponseEntity<?> paymentWebhook(@RequestBody ChargeResult chargeResult){

        log.info("webhoost {}",chargeResult);
        topUpService.verifyPaymentWebhookResponse(chargeResult);
        return new ResponseEntity<>("ok",HttpStatus.OK);

    }

    /* DASHBOARD ADMINSTRATOR ROUTES */
    @GetMapping("users/topups")
    public ResponseEntity<?> fetchAllTopUps(){
        return new ResponseEntity<>(topupRepository.findAll(),HttpStatus.OK);
    }



    @GetMapping("users/{id}/topups")
    public ResponseEntity<?> fetchAllTopUpsByUserId(@PathVariable long id){
        return new ResponseEntity<>(topupRepository.findAllByUserId(id),HttpStatus.OK);
    }
}
