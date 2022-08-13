package com.softport.meenvspringboot.user;

import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements   UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user) ;
    }

    @Override
    public User updateUser(User newUserInfo) {
        /*
         * merge existing info with incoming user object.
         * set incoming object's password with existing password
         * set incoming object's smsSent and smsBalance with existing ones to prevent
         * the possibility of the user supplying new balance to cheat the system.
         * */

        User loggedInUserData  = AuthenticationService.getAuthenticatedUser();
        User existingUserInfo  = userRepository.findById(loggedInUserData.getId())
                .orElseThrow(()-> new AppException("User not found", HttpStatus.BAD_REQUEST));

        newUserInfo.setPassword(existingUserInfo.getPassword());

        return userRepository.save(newUserInfo);
    }

    @Override
    public boolean canSendSMS(int numRecipients,int balance) {

        return  balance > numRecipients ;
    }


}
