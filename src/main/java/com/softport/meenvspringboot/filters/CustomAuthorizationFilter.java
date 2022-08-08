package com.softport.meenvspringboot.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softport.meenvspringboot.dto.ErrorDTO;
import com.softport.meenvspringboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Slf4j
@Order
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;

    private List<String> ignoredRoutes = List.of("/login","/usersignup","/payment/hook");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Authorization filter running");
        System.out.println(request.getServletPath());

        if(ignoredRoutes.contains(request.getServletPath())){
            log.info("mathc");
            filterChain.doFilter(request,response);
        }
        else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                log.info("Token found");
                try{
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256("somesecret".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);

                    // here phone number is the username
                    String username = decodedJWT.getSubject();

                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username,null,authorities);

                    authenticationToken.setDetails(userRepository.findByPhoneNumber(username));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request,response);
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
                log.info("token not found");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                new ObjectMapper().writeValue(response.getOutputStream(),
                        new ErrorDTO("Authorization token not found",
                                HttpStatus.UNAUTHORIZED.value(), new Date().toGMTString())
                );
                filterChain.doFilter(request, response);
            }
        }
    }
}
