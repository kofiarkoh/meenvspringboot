package com.softport.meenvspringboot.group;

import com.softport.meenvspringboot.dto.Group;
import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.services.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.softport.meenvspringboot.user.User;
import com.softport.meenvspringboot.repositories.GroupRepository;
import com.softport.meenvspringboot.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class GroupController {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @PostMapping(value = "/groups")
    public ResponseEntity<?> creatUserGroup(@Valid @RequestBody Groups group) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getDetails();
        group.setUserId(user.getId());
        group = groupRepository.save(group);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    @GetMapping("/groups")
    public ResponseEntity<?> getUserGroups() {

        User user = AuthenticationService.getAuthenticatedUser();
        String jpqlQuery = "SELECT new com.softport.meenvspringboot.dto.Group(g.id, g.name, COUNT(m)) FROM com.softport.meenvspringboot.group.Groups g LEFT JOIN g.contacts m WHERE g.userId = :userId GROUP BY g.id ORDER BY g.id";

        TypedQuery<Group> query = entityManager.createQuery(jpqlQuery, Group.class);

        query.setParameter("userId", user.getId());
        List<Group> results = query.getResultList();

        return new ResponseEntity<>(results, HttpStatus.CREATED);
    }

    @PostMapping(value = "groups/contacts")
    public ResponseEntity<Groups> updateGroup(@Valid @RequestBody Groups group) {
        /*
        * add user id to group to cater for instances when user_id isn't part
        * of request body.
        * */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getDetails();
        group.setUserId(user.getId());
        groupRepository.save(group);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "groups/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {
        if (groupRepository.existsById(groupId)) {
            groupRepository.deleteById(groupId);
            return new ResponseEntity<>("Group deleted succesfully", HttpStatus.OK);
        } else {
            throw new AppException("Group not found", HttpStatus.NOT_FOUND);
        }

    }

}
