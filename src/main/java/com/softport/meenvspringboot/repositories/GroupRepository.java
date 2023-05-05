package com.softport.meenvspringboot.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softport.meenvspringboot.group.Group;

import java.util.List;

public interface GroupRepository extends CrudRepository<Group, Long> {

    List<Group> findAllByUserId(long userId);

    @Query("select new com.softport.meenvspringboot.group.GroupPOGO(g.id,g.name, COUNT(m)) from Group g LEFT JOIN g.contacts m GROUP BY g.id ORDER BY g.id")
    List<?> findAllkio();

}