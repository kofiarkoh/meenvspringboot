package com.softport.meenvspringboot.user;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softport.meenvspringboot.dto.ErrorDTO;
import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.services.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    public ResponseEntity<UserWithMiscData> getUser(){
        UserWithMiscData data = new UserWithMiscData();
        data.setUser(userRepository.findById(AuthenticationService.getAuthenticatedUser().getId()).get());

        return new ResponseEntity(data, HttpStatus.CREATED);
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


    @GetMapping("user/refresh_token")
    public void refreshToken(HttpServletResponse response, HttpServletRequest request) throws IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("ref_token {}",authorizationHeader.substring("Bearer ".length()) );
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){

            try{
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("somesecret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);

                // here phone number is the username
                String username = decodedJWT.getSubject();
                User user = userRepository.findByPhoneNumber(username);

                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

                String accessToken = JWT.create().withSubject(user.getUsername())
                        // System.currentTimeMillis() + minutes * 60 * 1000
                        .withExpiresAt(new Date(System.currentTimeMillis() + 1 * 60 * 1000))
                        .withClaim("roles",authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .withIssuer(request.getRequestURL().toString())

                        .sign(algorithm);
                Map<String, Object> data =  new HashMap<>();
                data.put("user", user);
                data.put("accessToken", accessToken);
                data.put("refreshToken", refreshToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), data);
            }
            catch (Exception e){


                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(400);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        new ErrorDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value(), new Date().toGMTString())
                );
            }
        }
        else {
            throw new AppException("No refresh token provided",HttpStatus.BAD_REQUEST);
        }

    }
}
