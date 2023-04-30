package com.softport.meenvspringboot.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = { "phoneNumber" }))
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "user_id")
    private long id;

    @Column
    @Length(min = 2, message = "First name must be at least 3 characters")
    private String firstName;

    @Column
    @Length(min = 2, message = "Last name must be at least 3 characters")
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
    private long smsSent = 0;

    @Column
    private Date createdAt = new Date();

    @Column
    private Date emailVerifiedAt = new Date();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /*
     * @OneToMany(cascade = CascadeType.ALL)
     * // @JoinColumn(name = "user_id")
     * private List<Groups> groups;
     */

}
