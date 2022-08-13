package com.softport.meenvspringboot.user;

import com.softport.meenvspringboot.user.User;

public interface UserService {
    User saveUser(User user);

    User updateUser(User newUserInfo);

}
