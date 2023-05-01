package com.softport.meenvspringboot.OTP;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softport.meenvspringboot.user.User;

public interface OTPRepository extends CrudRepository<OTP, Long> {

    OTP findByCode(String code);

    List<OTP> deleteByUser(User user);
}
