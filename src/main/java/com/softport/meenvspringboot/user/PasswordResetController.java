package com.softport.meenvspringboot.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.softport.meenvspringboot.services.AuthenticationService;
import com.softport.meenvspringboot.services.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class PasswordResetController {
    private final UserRepository userRepository;

    private final OTPService otpService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @PostMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDTO data) {

        User user = userRepository.findByPhoneNumber(data.getPhoneNumber());
        if (user == null) {
            throw new AppException("No account found for the phone number provided", HttpStatus.NOT_FOUND);
        }
        OTP otp = otpService.generate(
                user.getPhoneNumber(),
                this.passwordEncoder.encode(data.getNewPassword()),
                user);

        String message = String.format("Your password reset token is %s", otp.getCode());
        emailService.sendMail(user.getEmail(), "MEENV: Password Reset", message);

        return new ResponseEntity<>(
                "Please verify your email by confirming the OTP sent to you",
                HttpStatus.OK);
    }

    @PostMapping("reset-password/{otp}/verify")
    public ResponseEntity<?> verifiIdentity(@PathVariable String otp) {
        OTP otpData = otpService.getOTP(otp);
        if (otpData.isExpired()) {
            throw new AppException(
                    "The provided otp has expired.",
                    HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByPhoneNumber(otpData.getFirstIdentifier());

        // update password
        user.setPassword(otpData.getSecondIdentifier());
        userRepository.save(user);

        otpService.deleteOTP(otpData);

        return new ResponseEntity<>(
                "Password reset successful.",
                HttpStatus.OK);

    }

}
