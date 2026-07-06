package com.hoane.fbhelper.fbhelperextentionbe.controller;

import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DataGroupPostDetailRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DataGroupPostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DataGroupPostDetailResponse;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DataGroupPostResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPostDetail;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data-group-posts")
public class DataGroupPostController {


    @Autowired
    private DataGroupPostService dataGroupPostService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<DataGroupPost>>> getDataGroupPosts() {

        int user_id = Constant.USER_ID;

        List<DataGroupPost> dataGroupPosts = dataGroupPostService.findAllByUser(user_id);

        return ApiResponse.success(200, "Get list data group post successfully", dataGroupPosts);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DataGroupPostResponse>> createDataGroupPost(@Validated(OnCreate.class) @RequestBody DataGroupPostRequest request) {

        int user_id = Constant.USER_ID;

        DataGroupPost dataGroupPost = dataGroupPostService.createDataGroupPost(request, user_id);

        return ApiResponse.success(201, "Create data group post successfully", new DataGroupPostResponse(dataGroupPost));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<DataGroupPostResponse>> updateDataGroupPost(@PathVariable int id, @Validated(OnUpdate.class) @RequestBody DataGroupPostRequest request) {

        DataGroupPostResponse dataGroupPostResponse = dataGroupPostService.update(id, request);

        return ApiResponse.success(200, "Update data group post successfully", dataGroupPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDataGroupPost(@PathVariable int id) {

        dataGroupPostService.delete(id);

        return ApiResponse.success(200, "Delete data group post successfully", null);
    }

    @GetMapping("/details/{device_id}")
    public ResponseEntity<ApiResponse<List<DataGroupPostDetailResponse>>> getDataGroupPostDetails(@PathVariable String device_id) {

        List<DataGroupPostDetailResponse> dataGroupPostDetails = dataGroupPostService.findAllDetailsByDeviceId(device_id).stream()
                .map(DataGroupPostDetailResponse::new)
                .collect(Collectors.toList());

        return ApiResponse.success(200, "Get list data group post details successfully", dataGroupPostDetails);
    }

    @PostMapping("/details")
    public ResponseEntity<ApiResponse<DataGroupPostDetailResponse>> createDataGroupPostDetail(@Valid @RequestBody DataGroupPostDetailRequest request) {

        DataGroupPostDetail dataGroupPostDetail = dataGroupPostService.createDataGroupPostDetail(request);

        return ApiResponse.success(201, "Create data group post detail successfully", new DataGroupPostDetailResponse(dataGroupPostDetail));
    }

    @PatchMapping("/details")
    public ResponseEntity<ApiResponse<DataGroupPostDetailResponse>> updateDataGroupPostDetail(@Valid @RequestBody DataGroupPostDetailRequest request) {

        DataGroupPostDetail dataGroupPostDetail = dataGroupPostService.updateDataGroupPostDetail(request);

        return ApiResponse.success(200, "Update data group post detail successfully", new DataGroupPostDetailResponse(dataGroupPostDetail));
    }

}
