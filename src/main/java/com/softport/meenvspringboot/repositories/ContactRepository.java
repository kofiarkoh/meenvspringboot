package com.softport.meenvspringboot.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softport.meenvspringboot.group.Contact;

public interface ContactRepository extends CrudRepository<Contact, Long> {

}
