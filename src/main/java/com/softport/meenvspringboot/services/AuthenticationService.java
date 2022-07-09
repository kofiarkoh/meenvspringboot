package com.softport.meenvspringboot.services;

import com.softport.meenvspringboot.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationService {
    public static User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  (User) authentication.getDetails();
    }
}
