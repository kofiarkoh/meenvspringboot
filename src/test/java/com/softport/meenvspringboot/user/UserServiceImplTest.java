package com.softport.meenvspringboot.user;

import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setId(22222);

    }


    @Test
    void user_updated_correctly() {
        try(MockedStatic authServiceMocked= mockStatic(AuthenticationService.class)){

            User existingUserInfo = new User();
            existingUserInfo.setId(user.getId());
            existingUserInfo.setFirstName("lawrence");
            existingUserInfo.setLastName("arkoh");
            existingUserInfo.setSmsBalance(200);
            existingUserInfo.setSmsSent(40);
            existingUserInfo.setPhoneNumber("1111111111");
            existingUserInfo.setEmail("kofi@gmail.com");
            existingUserInfo.setPassword("oldpassw");


            // make modification to user data
            user.setPassword("newpassword");
            user.setSmsBalance(200);
            user.setSmsSent(150);
            user.setEmail("kofi23@gmail.com");
            user.setFirstName("arkoh");
            user.setLastName("lawrence");

            authServiceMocked.when(AuthenticationService::getAuthenticatedUser).thenReturn(existingUserInfo);
            when(userRepository.findById(user.getId())).thenReturn(Optional.of(existingUserInfo));
            //when(userRepository.save(user)).thenReturn(user);

            userService.updateUser(user);
            ArgumentCaptor<User> userArgCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userArgCaptor.capture());
            User capturedUser = userArgCaptor.getValue();




            // password should not change since this is not mean to alter user's password
            // smsBalance and smsSent must also not change
            assertThat(capturedUser.getPassword()).isEqualTo("oldpassw");
            assertThat(capturedUser.getSmsSent()).isEqualTo(150);
            assertThat(capturedUser.getSmsBalance()).isEqualTo(200);


            // other info is correctly updated

        }
    }
}