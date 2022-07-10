package com.softport.meenvspringboot.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController @Slf4j
@RequestMapping("/upload")
public class SampleFileUploadController {

    @PostMapping
    public ResponseEntity<?> handleFileUpload(@RequestParam("photo") MultipartFile file){
        try{
            String randomeFileName = String.format("%06d",new Random().nextInt(999999));
            String filename = file.getOriginalFilename().replaceAll("\\s","_");
            Path basePath = Paths.get(System.getProperty("user.home")+ "/Desktop/vmshare");
            log.info("base path is {}",basePath);
            log.info("paths exists {} ",Files.exists(basePath));
            if (Files.exists(basePath)){
                file.transferTo(new File(basePath + "/" + randomeFileName + ".jpeg"));
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<?> getAllFileUrl(HttpServletResponse response){
        List<String> urls = new ArrayList<>();
        Path basePath = Paths.get(System.getProperty("user.home")+ "/Desktop/vmshare");
        File folder = basePath.toFile();
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (!file.getName().equals(".DS_Store")){
                    urls.add(file.getName());
                }

            } else if (file.isDirectory()) {
                System.out.println("Directory " + file.getName());
            }
        }

        return new ResponseEntity<>(urls, HttpStatus.OK);
    }
}
