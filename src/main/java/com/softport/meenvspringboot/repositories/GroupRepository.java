package com.softport.meenvspringboot.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softport.meenvspringboot.group.Groups;

import java.util.List;

public interface GroupRepository extends CrudRepository<Groups, Long> {

    List<Groups> findAllByUserId(long userId);

}