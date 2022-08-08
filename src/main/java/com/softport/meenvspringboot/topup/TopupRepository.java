package com.softport.meenvspringboot.topup;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TopupRepository extends CrudRepository<TopUp,Long> {
     Optional<TopUp> findByOtp(long otp);

     List<TopUp> findAllByUserId(long userId);
     TopUp findByTransactionId(String transId);
}
