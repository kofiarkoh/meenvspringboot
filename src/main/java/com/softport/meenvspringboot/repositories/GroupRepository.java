package com.softport.meenvspringboot.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softport.meenvspringboot.group.Group;

import java.util.List;

public interface GroupRepository extends CrudRepository<Group, Long> {

    List<Group> findAllByUserId(long userId);

}