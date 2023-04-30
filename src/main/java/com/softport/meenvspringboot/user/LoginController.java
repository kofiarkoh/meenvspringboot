package com.softport.meenvspringboot.user;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softport.meenvspringboot.OTP.OTPService;
import com.softport.meenvspringboot.dto.ErrorDTO;
import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.services.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("auth")
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;

    @GetMapping("refresh_token")
    public void refreshToken(HttpServletResponse response, HttpServletRequest request) throws IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("ref_token {}", authorizationHeader.substring("Bearer ".length()));
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {
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

                        .withExpiresAt(new Date(System.currentTimeMillis() + 1 * 60 * 1000))
                        .withClaim("roles",
                                authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .withIssuer(request.getRequestURL().toString())

                        .sign(algorithm);
                Map<String, Object> data = new HashMap<>();
                data.put("user", user);
                data.put("accessToken", accessToken);
                data.put("refreshToken", refreshToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), data);
            } catch (Exception e) {

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(400);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        new ErrorDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value(), new Date().toGMTString()));
            }
        } else {
            throw new AppException("No refresh token provided", HttpStatus.BAD_REQUEST);
        }

    }

}
