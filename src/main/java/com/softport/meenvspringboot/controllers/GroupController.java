package com.softport.meenvspringboot.controllers;

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

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @PostMapping(value = "/groups")
    public ResponseEntity<Groups> creatUserGroup(@RequestBody Groups group) {

        /*
         * Optional<User> user = userRepository.findById(1L);
         * if (user.isPresent()) {
         * group.setUser(user.get());
         * }
         */
        group = groupRepository.save(group);

        // User user = userRepository.findById(group.getId())

        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    @PostMapping(value = "groups/contacts")
    public ResponseEntity<Groups> updateGroupContacts(@RequestBody Groups group) {
        groupRepository.save(group);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    @GetMapping(value = "groups/{groupId}")
    public ResponseEntity<?> updateGroup(@PathVariable Long groupId) {

        return new ResponseEntity<>(groupRepository.findById(groupId), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "groups/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {
        groupRepository.deleteById(groupId);
        return new ResponseEntity<>("Group deleted succesfully", HttpStatus.OK);
    }

}
