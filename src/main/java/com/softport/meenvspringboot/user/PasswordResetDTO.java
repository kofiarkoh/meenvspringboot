package com.softport.meenvspringboot.user;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String phoneNumber;
    private String newPassword;
}
