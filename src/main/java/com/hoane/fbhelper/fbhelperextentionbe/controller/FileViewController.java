package com.hoane.fbhelper.fbhelperextentionbe.controller;

import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileViewController {

    @GetMapping("/uploads/{fileName}")
    public ResponseEntity<Resource> view(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(Constant.PATH_UPLOAD_DIR).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                String contentType = "image/jpeg";
                if (fileName.endsWith(".png")) {
                    contentType = "image/png";
                } else if (fileName.endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (fileName.endsWith(".pdf")) {
                    contentType = "application/pdf";
                }
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
            }
            throw new RuntimeException("File not found");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
