package com.softport.meenvspringboot.user;

public interface UserService {
    User saveUser(User user);

    User updateUser(User newUserInfo);

    boolean canSendSMS(int numRecipients, int balance);

}
