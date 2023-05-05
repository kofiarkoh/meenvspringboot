package com.softport.meenvspringboot.group;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softport.meenvspringboot.dto.Group;
import com.softport.meenvspringboot.exceptions.AppException;
import com.softport.meenvspringboot.repositories.ContactRepository;
import com.softport.meenvspringboot.repositories.GroupRepository;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.services.AuthenticationService;
import com.softport.meenvspringboot.services.ContactService;
import com.softport.meenvspringboot.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class GroupController {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final ContactService contactService;
    private final EntityManager entityManager;

    @PostMapping(value = "/groups")
    public ResponseEntity<?> creatUserGroup(@Valid @RequestBody Groups group) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getDetails();
        group.setUserId(user.getId());
        group = groupRepository.save(group);
        return new ResponseEntity<>(new Group(group.getId(), group.getName(), 0L), HttpStatus.CREATED);
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

    @PostMapping(value = "groups/{groupId}/contacts")
    public ResponseEntity<?> updateGroup(@PathVariable Long groupId, @Valid @RequestBody Contacts data) {
        return new ResponseEntity<>(contactService.addContact(groupId, data), HttpStatus.CREATED);
    }

    @GetMapping(value = "groups/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable Long groupId) {

        Optional<Groups> groups = groupRepository.findById(groupId);
        if (groups.isEmpty()) {
            throw new AppException("Group not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(groups.get(), HttpStatus.OK);
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
