package com.softport.meenvspringboot.templates;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Template {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private long userId;

    @Column
    @Length(min = 1, max = 150)
    private String message;
}
