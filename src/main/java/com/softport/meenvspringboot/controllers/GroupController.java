package com.softport.meenvspringboot.controllers;

import com.softport.meenvspringboot.exceptions.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.softport.meenvspringboot.models.Groups;
import com.softport.meenvspringboot.models.User;
import com.softport.meenvspringboot.repositories.GroupRepository;
import com.softport.meenvspringboot.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class GroupController {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @PostMapping(value = "/groups")
    public ResponseEntity<?> creatUserGroup(@RequestBody Groups group) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getDetails();
        group.setUserId(user.getId());
        group = groupRepository.save(group);
        // gdgdf ffsdsd dsdsd
        // User user = userRepository.findById(group.getId())

        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    @PostMapping(value = "groups/contacts")
    public ResponseEntity<Groups> updateGroupContacts(@Valid @RequestBody Groups group) {
        groupRepository.save(group);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    @GetMapping(value = "groups/{groupId}")
    public ResponseEntity<?> updateGroup( @PathVariable Long groupId) {
        System.out.println(groupId);
        Optional<Groups> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            throw new AppException("Group not found",HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "groups/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {
        groupRepository.deleteById(groupId);
        return new ResponseEntity<>("Group deleted succesfully", HttpStatus.OK);
    }

}
