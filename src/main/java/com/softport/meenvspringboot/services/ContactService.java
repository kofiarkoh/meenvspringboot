package com.softport.meenvspringboot.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.group.Contact;
import com.softport.meenvspringboot.group.Group;
import com.softport.meenvspringboot.repositories.ContactRepository;
import com.softport.meenvspringboot.repositories.GroupRepository;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final GroupRepository groupRepository;
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public Contact addContact(Long groupId, Contact data) {
        Optional<Group> groups = groupRepository.findById(groupId);
        if (groups.isEmpty()) {
            throw new AppException("Group not found", HttpStatus.NOT_FOUND);
        }

        data.setGroup(groups.get());

        contactRepository.save(data);

        return data;
    }

    @Transactional
    public Group createGroup(Group group) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getDetails();
        user = userRepository.findById(user.getId()).get();
        group.setUser(user);
        group = groupRepository.save(group);
        return group;
    }
}
