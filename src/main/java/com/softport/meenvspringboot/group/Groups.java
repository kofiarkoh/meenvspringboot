package com.softport.meenvspringboot.group;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    @Length(min = 3, message = "Group name must be at least 3 characters")
    private String name;

    /* @ManyToOne(cascade = CascadeType.ALL) */
    @Column(name = "user_id")
    private long userId;

    @OneToMany(cascade = CascadeType.ALL)
    private Collection<@Valid Contacts> contacts;

}
