package com.softport.meenvspringboot.user;

import java.util.Collection;
import java.util.Date;
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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
    @Length(min = 3, message = "First name must be at least 3 characters")
    private String firstName;

    @Column
    @Length(min = 3, message = "Last name must be at least 3 characters")
    private String lastName;

    @Column
    @NotNull
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits long")
    private String phoneNumber;

    @Column
    private String email = "";

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column
    private long smsBalance = 0;



    @Column
    private  long smsSent = 0;

    @Column
    private Date createdAt = new Date();

    /*
     * @OneToMany(cascade = CascadeType.ALL)
     * // @JoinColumn(name = "user_id")
     * private List<Groups> groups;
     */

}
