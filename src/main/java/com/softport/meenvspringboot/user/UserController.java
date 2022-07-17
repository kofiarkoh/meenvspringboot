package com.softport.meenvspringboot.user;

import java.util.Optional;

import javax.validation.Valid;

import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softport.meenvspringboot.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<User> signUp(@RequestBody @Valid User user) {
        if ( userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new AppException("Phone Number taken by another user",HttpStatus.BAD_REQUEST);
        }

        user = userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }

    @GetMapping("users/{userID}")
    public ResponseEntity<?> getUser(@PathVariable Long userID) {
        Optional<User> user = userRepository.findById(userID);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("users")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User newUserInfo) {
        /*
        * merge existing info with incoming user object.
        * set incoming object's password with existing password
        * set incoming object's smsSent and smsBalance with existing ones to prevent
        * the possibility of the user supplying new balance to cheat the system.
        * */
        User loggedInUserData  = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        User existingUserInfo  = userRepository.findById(loggedInUserData.getId())
                .orElseThrow(()-> new AppException("User not found",HttpStatus.BAD_REQUEST));
        newUserInfo.setPassword(existingUserInfo.getPassword());
        return new ResponseEntity<>( userRepository.save(newUserInfo), HttpStatus.CREATED);
    }

}
