package com.softport.meenvspringboot.user;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.softport.meenvspringboot.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;



    @PostMapping("/usersignup")
    public ResponseEntity<User> signUp(
            HttpServletResponse httpServletResponse,
            @RequestBody @Valid User user) throws IOException {
        if ( userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new AppException("Phone Number taken by another user",HttpStatus.BAD_REQUEST);
        }

        user = userService.saveUser(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }

    @GetMapping("user")
    public ResponseEntity<User> getUser(){
        return new ResponseEntity(userRepository.findById(AuthenticationService.getAuthenticatedUser().getId()).get(), HttpStatus.CREATED);
    }
    @GetMapping("users/{userID}")
    public ResponseEntity<?> getUser(@PathVariable Long userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(()-> new AppException("User not found",HttpStatus.NOT_FOUND));

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PatchMapping("user")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User newUserInfo) {


        return new ResponseEntity<>( userService.updateUser(newUserInfo), HttpStatus.CREATED);
    }

    @GetMapping("users/all")
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<>(userRepository.findAll(),HttpStatus.OK);
    }

}
