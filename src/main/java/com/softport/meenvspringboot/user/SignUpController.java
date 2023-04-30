package com.softport.meenvspringboot.user;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softport.meenvspringboot.OTP.OTP;
import com.softport.meenvspringboot.OTP.OTPService;
import com.softport.meenvspringboot.dto.SendOTP;
import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.services.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SignUpController {

    private final OTPService otpService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<User> signUp(
            HttpServletResponse httpServletResponse,
            @RequestBody @Valid User user) throws IOException {
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new AppException("Phone Number taken by another user", HttpStatus.BAD_REQUEST);
        }

        user = userService.saveUser(user);

        OTP otp = otpService.generate(
                user.getPhoneNumber(),
                user.getEmail());

        String message = String.format("Your email verification token is %s", otp.getCode());
        emailService.sendMail("lawrencearkoh6@gmail.com", "MEENV: Email Verification", message);

        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }

    @PostMapping("signup/{otp}/verify")
    public ResponseEntity<?> verifyEmail(@PathVariable String otp) {
        OTP otpData = otpService.getOTP(otp);
        User user = userRepository.findByPhoneNumber(otpData.getFirstIdentifier());

        user.setEmailVerifiedAt(new Date());
        userRepository.save(user);

        return new ResponseEntity<>("Verification successfull", HttpStatus.OK);

    }

    @PostMapping("signup/send-verification")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody SendOTP data) {

        User user = userRepository.findByPhoneNumber(data.getUsername());
        if (user == null) {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }
        OTP otp = otpService.generate(
                user.getPhoneNumber(),
                user.getEmail());

        String message = String.format("Your email verification token is %s",
                otp.getCode());
        emailService.sendMail("lawrencearkoh6@gmail.com",
                "MEENV: Email Verification", message);

        user.setEmailVerifiedAt(new Date());
        userRepository.save(user);

        return new ResponseEntity<>("Verification sent successfully", HttpStatus.OK);

    }

}