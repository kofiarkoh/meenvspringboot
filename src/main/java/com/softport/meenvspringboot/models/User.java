package com.softport.meenvspringboot.models;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "phoneNumber" }))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    @NotNull
    @Size(min = 10, max = 10)
    // @UniqueConstraint
    private String phoneNumber;

    @Column
    private String password;

    /*
     * @OneToMany(cascade = CascadeType.ALL)
     * // @JoinColumn(name = "user_id")
     * private List<Groups> groups;
     */

}
