package com.softport.meenvspringboot.user;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.services.AuthenticationService;
import com.softport.meenvspringboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.softport.meenvspringboot.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

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
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new AppException("Phone Number taken by another user", HttpStatus.BAD_REQUEST);
        }

        user = userService.saveUser(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }

    @GetMapping("user")
    public ResponseEntity<User> getUser() {
        return new ResponseEntity<User>(
                userRepository.findById(AuthenticationService.getAuthenticatedUser().getId()).get(),
                HttpStatus.CREATED);
    }

    @GetMapping("users/{userID}")
    public ResponseEntity<?> getUser(@PathVariable Long userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PatchMapping("user")
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

    @GetMapping("users/all")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

}
