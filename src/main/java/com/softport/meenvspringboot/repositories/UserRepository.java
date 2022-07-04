package com.softport.meenvspringboot.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softport.meenvspringboot.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByPhoneNumber(String phoneNumber);
}
