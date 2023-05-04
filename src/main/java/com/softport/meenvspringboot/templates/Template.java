package com.softport.meenvspringboot.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor()
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Template {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private long userId;

    @Column
    @NotBlank
    @Length(min = 10, max = 150)
    private String message;
}
