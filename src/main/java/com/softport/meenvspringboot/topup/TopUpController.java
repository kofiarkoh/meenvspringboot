package com.softport.meenvspringboot.topup;

import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.services.AuthenticationService;
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

    /*
    * request topup end point
    * */
    @PostMapping("user/topup")
    public ResponseEntity<?> requestTopUp(@RequestBody TopUp topUp){

        //generate unique transaction ID and OTP
        Random random = new Random();
        topUp.setTransactionId(String.valueOf(random.nextLong(999999)));
        long otp = random.nextLong(99999);
        topUp.setOtp(otp);
        topUp.setDate(new Date());
        topUp.setStatus("PENDING");
        topUp.setUserId(AuthenticationService.getAuthenticatedUser().getId());

        return new ResponseEntity<>(topupRepository.save(topUp),HttpStatus.OK);

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

        MomoRequestDTO body = new MomoRequestDTO(
                100,
                "kofarkoh0@gmail.com",
                "GHS",
                new MomoDTO(topupData.getPhoneNumber(),topupData.getNetwork().toLowerCase()) ,
                "s", topupData.getTransactionId()

        );

        InitTransactionResponse initTransactionResponse = PayStack.makePaymentReques(body);
        System.out.println(initTransactionResponse);
      //  if (initTransactionResponse.getData().get)
       return new ResponseEntity<>("Processing payment",HttpStatus.OK);
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

    /* DASHBOARD ADMINSTRATOR ROUTES */
    @GetMapping("users/topups")
    public ResponseEntity<?> fetchAllTopUps(){
        return new ResponseEntity<>(topupRepository.findAll(),HttpStatus.OK);
    }

    @PostMapping("payment/hook")
    public ResponseEntity<?> paymentWebhook(@RequestBody ChargeResult response){
        System.out.println(response);;
        TopUp topUp = topupRepository.findByTransactionId(response.getData().getReference());
        String status = response.getData().getStatus();
        if (status.equals("success") && !topUp.getStatus().equals("SUCCESS")){

            topUp.setStatus("SUCCESS");
            // update customer's balance
            User user =  userRepository.findById(topUp.getUserId()).get();
            user.setSmsBalance(user.getSmsBalance() + topUp.getSmsQuantity());
        }
        else if(status.equals("failed") || status.equals("timeout")){
            topUp.setStatus("FAILED");

        }

        topupRepository.save(topUp);
        return new ResponseEntity<>(response,HttpStatus.OK);

    }



    @GetMapping("users/{id}/topups")
    public ResponseEntity<?> fetchAllTopUpsByUserId(@PathVariable long id){
        return new ResponseEntity<>(topupRepository.findAllByUserId(id),HttpStatus.OK);
    }
}
