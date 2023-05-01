package com.softport.meenvspringboot.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softport.meenvspringboot.user.User;

public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    User findByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u")
    int userCount();

}
