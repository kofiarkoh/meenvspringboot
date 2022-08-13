package com.softport.meenvspringboot.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softport.meenvspringboot.user.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    User findByPhoneNumber(String phoneNumber);

    @Query("SELECT COUNT(u) FROM User u")
    int userCount();


}
