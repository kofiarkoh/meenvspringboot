package com.softport.meenvspringboot.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class ResourceHandler  implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //WebMvcConfigurer.super.addResourceHandlers(registry);
        //TODO extract basepath to a common place so that all other parts can reference.
        Path basePath = Paths.get(System.getProperty("user.home")+ "/Desktop/vmshare");
        System.out.println(basePath);

        registry.addResourceHandler("/vmshare/**").addResourceLocations("file:"+basePath.toString()+"/");
    }
}
