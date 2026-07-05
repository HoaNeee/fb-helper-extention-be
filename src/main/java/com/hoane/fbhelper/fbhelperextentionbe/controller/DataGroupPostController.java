package com.hoane.fbhelper.fbhelperextentionbe.controller;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DataGroupPostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DataGroupPostResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPost;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.DataGroupPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data-group-posts")
public class DataGroupPostController {

    @Autowired
    private DataGroupPostService dataGroupPostService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<DataGroupPost>>> getDataGroupPosts() {

        List<DataGroupPost> dataGroupPosts = dataGroupPostService.findAll();

        return ApiResponse.success(200, "Get list data group post successfully", dataGroupPosts);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DataGroupPostResponse>> createDataGroupPost(@Validated(OnCreate.class) @RequestBody DataGroupPostRequest request) {

        DataGroupPostResponse dataGroupPostResponse = dataGroupPostService.save(request);

        return ApiResponse.success(201, "Create data group post successfully", dataGroupPostResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<DataGroupPostResponse>> updateDataGroupPost(@PathVariable String id, @Validated(OnUpdate.class) @RequestBody DataGroupPostRequest request) {

        DataGroupPostResponse dataGroupPostResponse = dataGroupPostService.save(request);

        return ApiResponse.success(200, "Update data group post successfully", dataGroupPostResponse);
    }

}
