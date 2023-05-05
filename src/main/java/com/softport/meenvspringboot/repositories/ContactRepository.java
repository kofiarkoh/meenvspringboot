package com.softport.meenvspringboot.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softport.meenvspringboot.group.Contacts;

public interface ContactRepository extends CrudRepository<Contacts, Long> {

}
