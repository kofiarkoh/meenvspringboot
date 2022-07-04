package com.softport.meenvspringboot.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softport.meenvspringboot.models.Groups;

public interface GroupRepository extends CrudRepository<Groups, Long> {
}