package com.softport.meenvspringboot.services;

import com.softport.meenvspringboot.user.User;
import com.softport.meenvspringboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements   UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
       // this.loadUserByUsername("sds").
        return this.userRepository.save(user) ;
    }


}
