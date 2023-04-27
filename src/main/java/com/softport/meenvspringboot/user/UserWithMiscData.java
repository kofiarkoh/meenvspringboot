package com.softport.meenvspringboot.user;

import lombok.Data;

@Data
public class UserWithMiscData {
    private User user;
    private MiscData miscData = new MiscData();
}
