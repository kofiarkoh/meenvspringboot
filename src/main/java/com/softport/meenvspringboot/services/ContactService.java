package com.softport.meenvspringboot.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.group.Contacts;
import com.softport.meenvspringboot.group.Group;
import com.softport.meenvspringboot.repositories.ContactRepository;
import com.softport.meenvspringboot.repositories.GroupRepository;
import com.softport.meenvspringboot.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final GroupRepository groupRepository;
    private final ContactRepository contactRepository;

    public Contacts addContact(Long groupId, Contacts data) {
        Optional<Group> groups = groupRepository.findById(groupId);
        if (groups.isEmpty()) {
            throw new AppException("Group not found", HttpStatus.NOT_FOUND);
        }

        data.setGroup(groups.get());

        contactRepository.save(data);

        return data;
    }
}
