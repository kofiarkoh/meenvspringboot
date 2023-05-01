package com.softport.meenvspringboot.user;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softport.meenvspringboot.OTP.OTPService;
import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.services.AuthenticationService;
import com.softport.meenvspringboot.services.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final OTPService otpService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @GetMapping("{userID}")
    public ResponseEntity<?> getUser(@PathVariable Long userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User newUserInfo) {
        /*
         * merge existing info with incoming user object.
         * set incoming object's password with existing password
         * set incoming object's smsSent and smsBalance with existing ones to prevent
         * the possibility of the user supplying new balance to cheat the system.
         */
        User loggedInUserData = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        User existingUserInfo = userRepository.findById(loggedInUserData.getId())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.BAD_REQUEST));
        newUserInfo.setPassword(existingUserInfo.getPassword());
        return new ResponseEntity<>(userRepository.save(newUserInfo), HttpStatus.CREATED);
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("user")
    public ResponseEntity<User> getUser() {
        return new ResponseEntity<User>(
                userRepository.findById(AuthenticationService.getAuthenticatedUser().getId()).get(),
                HttpStatus.CREATED);
    }

}
