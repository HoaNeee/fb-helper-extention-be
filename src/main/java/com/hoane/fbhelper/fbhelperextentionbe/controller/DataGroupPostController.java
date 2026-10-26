package com.hoane.fbhelper.fbhelperextentionbe.controller;

import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.BasePageRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DataGroupPostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RequestModels;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DataGroupPostResponse;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.PageResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPostDetail;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Pagination;
import com.hoane.fbhelper.fbhelperextentionbe.entity.wrapper.CustomUserDetails;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.DataGroupPostService;
import com.hoane.fbhelper.fbhelperextentionbe.utils.SecurityContextUtils;
import jakarta.validation.Valid;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data-group-posts")
@PreAuthorize("principal.enabled")
public class DataGroupPostController {

    @Autowired
    private DataGroupPostService dataGroupPostService;


    @GetMapping
    public ResponseEntity<PageResponse<List<DataGroupPostResponse.DataGroupResponse>>> getDataGroupPostsByUser(@Valid @ModelAttribute BasePageRequest pageRequest, @RequestParam(required = false) String keyword) {

        String user_id = SecurityContextUtils.getUserIdFromContext();

        Page<DataGroupPost> dataGroupPosts = dataGroupPostService.findAllByUser(user_id, pageRequest.toPageable(), keyword);

        Pagination pagination = new Pagination(dataGroupPosts.getNumber(), dataGroupPosts.getSize(), dataGroupPosts.getTotalElements(), dataGroupPosts.getTotalPages(), dataGroupPosts.hasNext());

        return ResponseEntity.ok(new PageResponse<>(200, "Get list data group post successfully", true, dataGroupPosts.map(DataGroupPostResponse.DataGroupResponse::new).getContent(), pagination));
    }

    @GetMapping("/device/{device_id}")
    public ResponseEntity<PageResponse<List<DataGroupPostResponse.DataGroupResponse>>> getDataGroupPostsByDeviceId(@PathVariable String device_id, @Valid @ModelAttribute BasePageRequest pageRequest, @RequestParam(required = false) String keyword) {

        String user_id = SecurityContextUtils.getUserIdFromContext();

        DataGroupPostResponse.PageResponse pageResponse = dataGroupPostService.getListDataGroupPostAndDetails(user_id, device_id, pageRequest.toPageable(), keyword);

        return ResponseEntity.ok(new PageResponse<>(200, "Get list data group post successfully", true, pageResponse.getData(), pageResponse.getPagination()));
    }

    @GetMapping("/details/{device_id}")
    public ResponseEntity<ApiResponse<List<DataGroupPostResponse.DataGroupPostDetailResponse>>> getDataGroupPostDetails(@PathVariable String device_id) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        List<DataGroupPostResponse.DataGroupPostDetailResponse> dataGroupPostDetails = dataGroupPostService.findAllDetailsByDeviceIdAndUserId(device_id, userId).stream()
                .map(DataGroupPostResponse.DataGroupPostDetailResponse::new)
                .collect(Collectors.toList());

        return ApiResponse.success(200, "Get list data group post details successfully", dataGroupPostDetails);
    }

    @GetMapping("/get-max-priority")
    public ResponseEntity<ApiResponse<Integer>> getMaxPriority() {

        String userId = SecurityContextUtils.getUserIdFromContext();

        Integer maxPriority = dataGroupPostService.getMaxPriority(userId);
        return ApiResponse.success(200, "Get max priority successfully", maxPriority);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DataGroupPostResponse.DataGroupResponse>> createDataGroupPost(@Validated(OnCreate.class) @RequestBody DataGroupPostRequest.DataGroupRequest request) {

        String user_id = SecurityContextUtils.getUserIdFromContext();

        DataGroupPost dataGroupPost = dataGroupPostService.createDataGroupPost(request, user_id);

        return ApiResponse.success(201, "Create data group post successfully", new DataGroupPostResponse.DataGroupResponse(dataGroupPost));
    }

    @PostMapping("/details")
    public ResponseEntity<ApiResponse<DataGroupPostResponse.DataGroupPostDetailResponse>> createDataGroupPostDetail(@Valid @RequestBody DataGroupPostRequest.DataGroupPostDetailRequest request) {

        String user_id = SecurityContextUtils.getUserIdFromContext();

        DataGroupPostDetail dataGroupPostDetail = dataGroupPostService.createDataGroupPostDetail(user_id, request);

        return ApiResponse.success(201, "Create data group post detail successfully", new DataGroupPostResponse.DataGroupPostDetailResponse(dataGroupPostDetail));
    }

    @PostMapping("/import-data")
    public ResponseEntity<ApiResponse<List<DataGroupPostResponse.DataGroupResponse>>> importDataGroupPosts(@RequestBody RequestModels.DataGroupPostImportRequest request) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        List<DataGroupPost> dataGroupPosts = dataGroupPostService.importDataGroupPost(userId, request);
        List<DataGroupPostResponse.DataGroupResponse> listResponse = dataGroupPosts.stream().map(DataGroupPostResponse.DataGroupResponse::new).toList();

        return ApiResponse.success(200, "Import data group posts successfully", listResponse);
    }

    @PostMapping("/sync-device-data-group-post")
    public ResponseEntity<ApiResponse<Boolean>> syncDataGroupPostToDevice(@Valid @RequestBody RequestModels.DeviceSyncDataRequest request) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        Boolean success = dataGroupPostService.syncDataGroupPostToDevice(userId, request);

        return ApiResponse.success(200, "Sync data group post to device successfully", success);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<DataGroupPostResponse.DataGroupResponse>> updateDataGroupPost(@PathVariable String id, @Validated(OnUpdate.class) @RequestBody DataGroupPostRequest.DataGroupRequest request) {

        DataGroupPostResponse.DataGroupResponse dataGroupPostResponse = dataGroupPostService.updateDataGroupPost(id, request);

        return ApiResponse.success(200, "Update data group post successfully", dataGroupPostResponse);
    }

    @PatchMapping("/update-status-all-device/{id}")
    public ResponseEntity<ApiResponse<Void>> updateStatusAllDevice(@PathVariable String id, @Valid @RequestBody DataGroupPostRequest.DataGroupPostUpdateStatusAllDeviceRequest request) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        dataGroupPostService.updateStatusAllDevice(userId, id, request);

        return ApiResponse.success(200, "Update status all device successfully", null);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDataGroupPost(@PathVariable String id) {

        dataGroupPostService.delete(id);

        return ApiResponse.success(200, "Delete data group post successfully", null);
    }

    @DeleteMapping("/delete-all-data-group-post")
    public ResponseEntity<ApiResponse<Void>> deleteAllDataGroupPost() {

        String userId = SecurityContextUtils.getUserIdFromContext();

        dataGroupPostService.deleteAll(userId);

        return ApiResponse.success(200, "Delete all data group post by user successfully", null);
    }


    @PatchMapping("/details")
    public ResponseEntity<ApiResponse<DataGroupPostResponse.DataGroupPostDetailResponse>> updateDataGroupPostDetail(@Valid @RequestBody DataGroupPostRequest.DataGroupPostDetailRequest request) {

        DataGroupPostDetail dataGroupPostDetail = dataGroupPostService.updateDataGroupPostDetail(request);

        return ApiResponse.success(200, "Update data group post detail successfully", new DataGroupPostResponse.DataGroupPostDetailResponse(dataGroupPostDetail));
    }

}
