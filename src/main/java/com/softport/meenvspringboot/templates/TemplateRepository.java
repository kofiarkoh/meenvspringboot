package com.softport.meenvspringboot.templates;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TemplateRepository extends CrudRepository<Template,Long> {
    public List<Template> findTemplateByUserId(long userId);
}
