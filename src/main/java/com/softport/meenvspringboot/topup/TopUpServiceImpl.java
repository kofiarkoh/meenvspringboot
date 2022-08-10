package com.softport.meenvspringboot.topup;

import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

//@Transactional
@RequiredArgsConstructor()
@Service
@Slf4j
public class TopUpServiceImpl implements TopUpService {

 //   @Autowired
    private final   TopupRepository topupRepository;
   // @Autowired
    private final   UserRepository userRepository;

    @Override
    public boolean isOTPValid(TopUp topUpData) {
        /*
         * OTP is only valid for 5 mins
         * check if OTP is valid before procressing payment with Paystack.
         * */
        Instant now = Instant.now();
        Duration timeElapsed = Duration.between(topUpData.getDate().toInstant(),now);


        return timeElapsed.toMinutes() > 5 ;

    }

    @Override
    public void verifyPaymentWebhookResponse(ChargeResult response) {
        TopUp topUp = topupRepository.findByTransactionId(response.getData().getReference());

        if(topUp == null){
            // no item found , therefore return request silently without performing any operation
            return;
        }
        String status = response.getData().getStatus();
        if (status.equalsIgnoreCase("success") && !topUp.getStatus().equals("SUCCESS")){
            topUp.setStatus("SUCCESS");

            // update customer's balance
            Optional<User> userOptional = userRepository.findById(topUp.getUserId());
            if (userOptional.isPresent()){
                User user =  userOptional.get();
                user.setSmsBalance(user.getSmsBalance() + topUp.getSmsQuantity());
                userRepository.save(user);
            }


        }
        else if(status.equalsIgnoreCase("failed") || status.equals("timeout")){
            topUp.setStatus("FAILED");

        }


        topupRepository.save(topUp);
    }

}
