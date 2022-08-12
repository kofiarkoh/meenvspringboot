package com.softport.meenvspringboot.OTP;

import org.springframework.data.repository.CrudRepository;

public interface OTPRepository extends CrudRepository<OTP,Long> {

    OTP findByCode(String code);
}
