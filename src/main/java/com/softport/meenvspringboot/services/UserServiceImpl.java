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
public class UserServiceImpl implements   UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user) ;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.userRepository.findByPhoneNumber(username);
        if (user == null){
            throw  new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getPhoneNumber(),user.getPassword(), new ArrayList<>());
    }
}
