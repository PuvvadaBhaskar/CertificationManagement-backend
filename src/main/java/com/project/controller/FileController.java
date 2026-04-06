package com.project.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String path = UPLOAD_DIR + fileName;

            Files.copy(file.getInputStream(), Paths.get(path));

            return path;

        } catch (Exception e) {
            throw new RuntimeException("File upload failed");
        }
    }
}