package com.hoane.fbhelper.fbhelperextentionbe.controller;


import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
@PreAuthorize("principal.enabled")
public class FileController {

    @Autowired
    private FileService fileService;


    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        String fileUrl = fileService.storeFile(file, request);

        return ApiResponse.success(200, "File uploaded successfully", fileUrl);
    }

    @PostMapping("/uploads")
    public ResponseEntity<ApiResponse<List<String>>> uploads(@RequestParam("files") List<MultipartFile> files, HttpServletRequest request) {

        List<String> fileUrls = fileService.storeFile(files, request);

        return ApiResponse.success(200, "File uploaded successfully", fileUrls);
    }
}
