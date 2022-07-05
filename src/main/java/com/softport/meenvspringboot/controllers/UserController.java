package com.softport.meenvspringboot.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.softport.meenvspringboot.exceptions.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softport.meenvspringboot.models.Groups;
import com.softport.meenvspringboot.models.User;
import com.softport.meenvspringboot.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity<User> signUp(@RequestBody @Valid User user) {
        if ( userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new AppException("Phone Number taken by another user",HttpStatus.BAD_REQUEST);
        }

        user = userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }

    @GetMapping("users/{userID}")
    public ResponseEntity<?> getUser(@PathVariable Long userID) {
        return new ResponseEntity<>(userRepository.findById(userID), HttpStatus.CREATED);
    }

    @PutMapping("users")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        user = userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

}
