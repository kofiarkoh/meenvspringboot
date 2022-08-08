package com.softport.meenvspringboot.templates;

import com.softport.meenvspringboot.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TemplateController {

    private final TemplateRepository templateRepository;

    @PostMapping("templates")
    public ResponseEntity<Template> addTemplate(@RequestBody Template template){
        template.setUserId(AuthenticationService.getAuthenticatedUser().getId());
        return new ResponseEntity<>( templateRepository.save(template), HttpStatus.CREATED);
    }

    @GetMapping("templates")
    public ResponseEntity<List<Template>> getUserTemplates(){
        return new ResponseEntity<>(templateRepository.findTemplateByUserId(AuthenticationService.getAuthenticatedUser().getId()),HttpStatus.OK);
    }
}
