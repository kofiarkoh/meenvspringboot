package com.softport.meenvspringboot.templates;

import com.softport.meenvspringboot.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("templates")
public class TemplateController {

    private final TemplateRepository templateRepository;

    @PostMapping()
    public ResponseEntity<Template> addTemplate(@RequestBody @Valid Template template) {
        template.setUserId(AuthenticationService.getAuthenticatedUser().getId());
        return new ResponseEntity<>(templateRepository.save(template), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Template>> getUserTemplates() {
        return new ResponseEntity<>(
                templateRepository.findTemplateByUserId(AuthenticationService.getAuthenticatedUser().getId()),
                HttpStatus.OK);
    }
}
