package com.softport.meenvspringboot.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softport.meenvspringboot.dto.ErrorDTO;
import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.services.AuthenticationService;
import com.softport.meenvspringboot.user.MiscData;
import com.softport.meenvspringboot.user.User;
import jdk.jfr.ContentType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Data

class LoginDto{
    String username;
    String password;
}
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        ObjectMapper mapper= new ObjectMapper();
        try {
            LoginDto loginDto = mapper.readValue(request.getInputStream(), LoginDto.class);
            log.info("username is {} ",loginDto.getUsername());
            log.info("password is {} ",loginDto.getPassword());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            return this.authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("login success");

        com.softport.meenvspringboot.user.User user = (User) authResult.getPrincipal();

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));


        Algorithm algorithm = Algorithm.HMAC256("somesecret".getBytes());
        String accessToken = JWT.create().withSubject(user.getUsername())
                // System.currentTimeMillis() + minutes * 60 * 1000
                .withExpiresAt(new Date(System.currentTimeMillis() + 100 * 60 * 1000))
                .withClaim("roles",authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        String refreshToken = JWT.create().withSubject(user.getUsername())
                // 43800 minutes i.e expires after 1 month
                .withExpiresAt(new Date(System.currentTimeMillis() + 43800L * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Map<String, Object> data =  new HashMap<>();
        data.put("user", user);
        data.put("accessToken", accessToken);
        data.put("refreshToken", refreshToken);
        data.put("miscData",new MiscData());


        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), data);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.error("Login failure");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),new ErrorDTO("Incorrect Username and Password", HttpStatus.BAD_REQUEST.value(), new Date().toGMTString()));
    }
}
