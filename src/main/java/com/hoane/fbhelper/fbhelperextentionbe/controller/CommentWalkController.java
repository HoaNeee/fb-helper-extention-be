package com.hoane.fbhelper.fbhelperextentionbe.controller;


import com.hoane.fbhelper.fbhelperextentionbe.dto.request.BasePageRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.CommentWalkRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RequestModels;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.CommentWalkResponse;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.PageResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.*;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.CommentWalkService;
import com.hoane.fbhelper.fbhelperextentionbe.utils.SecurityContextUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment-walks")
@PreAuthorize("@userService.member")
public class CommentWalkController {
    @Autowired
    private CommentWalkService commentWalkService;

    @GetMapping
    public ResponseEntity<PageResponse<List<CommentWalkResponse.DataResponse>>> getCommentWalks(@Valid @ModelAttribute BasePageRequest basePageRequest, @RequestParam(required = false) String keyword) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        Page<CommentWalk> res = commentWalkService.findAllByUser(userId, basePageRequest.toPageable(), keyword);

        Pagination pagination = new Pagination(res.getNumber(), res.getSize(), res.getTotalElements(), res.getTotalPages(), res.hasNext());

        return ResponseEntity.ok(new PageResponse<>(200, "Get list comment walk successfully", true, res.map(CommentWalkResponse.DataResponse::new).getContent(), pagination));
    }

    @GetMapping("/device/{device_id}")
    public ResponseEntity<PageResponse<List<CommentWalkResponse.DataResponse>>> getCommentWalkByDevice(@PathVariable String device_id, @Valid @ModelAttribute BasePageRequest basePageRequest, @RequestParam(required = false) String keyword) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        CommentWalkResponse.PageResponse pageResponse = commentWalkService.getListCommentWalkAndDetails(userId, device_id, basePageRequest.toPageable(), keyword);


        return ResponseEntity.ok(new PageResponse<>(200, "Get list comment walk by device successfully", true, pageResponse.getData(), pageResponse.getPagination()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentWalkResponse.DataResponse>> getCommentWalkById(@PathVariable String id) {

        CommentWalk commentWalk = commentWalkService.findCommentWalkByIdOrThrow(id);

        return ApiResponse.success(200, "Get comment walk successfully", new CommentWalkResponse.DataResponse(commentWalk));
    }

    @GetMapping("/list-id-comment-walk-active/{deviceId}")
    public ResponseEntity<ApiResponse<List<String>>> getListIdCommentWalkActive(@PathVariable String deviceId) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        List<String> list = commentWalkService.getListIdCommentWalkActive(userId, deviceId);

        return ApiResponse.success(200, "Get list comment walk active id successfully", list);
    }


    @GetMapping("/list-comment-walk-active/{deviceId}")
    public ResponseEntity<ApiResponse<List<CommentWalkResponse.DataResponse>>> getListCommentWalkActive(@PathVariable String deviceId) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        List<CommentWalk> list = commentWalkService.getListCommentWalkActive(userId, deviceId);

        return ApiResponse.success(200, "Get list comment walk active id successfully", list.stream().map(CommentWalkResponse.DataResponse::new).toList());
    }


    @PostMapping
    public ResponseEntity<ApiResponse<CommentWalkResponse.DataResponse>> createCommentWalk(@Validated(OnCreate.class) @RequestBody CommentWalkRequest.BaseRequest request) {

        String user_id = SecurityContextUtils.getUserIdFromContext();

        CommentWalk commentWalk = commentWalkService.createCommentWalk(request, user_id);

        return ApiResponse.success(201, "Create comment walk successfully", new CommentWalkResponse.DataResponse(commentWalk));
    }

    @PostMapping("/import-data")
    public ResponseEntity<ApiResponse<List<CommentWalkResponse.DataResponse>>> importDataGroupPosts(@RequestBody RequestModels.CommentWalkImportRequest request) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        List<CommentWalk> commentWalks = commentWalkService.importCommentWalk(userId, request);
        List<CommentWalkResponse.DataResponse> listResponse = commentWalks.stream().map(CommentWalkResponse.DataResponse::new).toList();

        return ApiResponse.success(201, "Import comment walks successfully", listResponse);
    }

    @PostMapping("/sync-device-comment-walk")
    public ResponseEntity<ApiResponse<Boolean>> syncCommentWalkToDevice(@Valid @RequestBody RequestModels.DeviceSyncDataRequest request) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        Boolean success = commentWalkService.syncCommentWalkToDevice(userId, request);

        return ApiResponse.success(200, "Sync comment walk to device successfully", success);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentWalkResponse.DataResponse>> updateCommentWalk(@PathVariable String id, @Validated(OnUpdate.class) @RequestBody CommentWalkRequest.BaseRequest request) {

        CommentWalk commentWalk = commentWalkService.updateCommentWalk(id, request);

        return ApiResponse.success(200, "Update comment walk successfully", new CommentWalkResponse.DataResponse(commentWalk));
    }

    @PatchMapping("/details")
    public ResponseEntity<ApiResponse<CommentWalkResponse.CommentWalkDetailResponse>> updateCommentWalkDetail(@Valid @RequestBody CommentWalkRequest.CommentWalkDetailRequest request) {

        CommentWalkDetail commentWalkDetail = commentWalkService.updateOrCreateCommentWalkDetail(request);

        return ApiResponse.success(200, "Update comment walk status successfully", new CommentWalkResponse.CommentWalkDetailResponse(commentWalkDetail));
    }

    @PatchMapping("/update-status-all-device/{id}")
    public ResponseEntity<ApiResponse<Void>> updateStatusCommentWalkAllDevice(@PathVariable String id, @Valid @RequestBody CommentWalkRequest.CommentWalkUpdateStatusAllDeviceRequest request) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        commentWalkService.updateStatusAllDevice(userId, id, request);
        return ApiResponse.success(204, "Update status comment walk all device successfully", null);
    }

    @DeleteMapping("/delete-all-comment-walks")
    public ResponseEntity<ApiResponse<Void>> deleteAllCommentWalksByUser() {

        String userId = SecurityContextUtils.getUserIdFromContext();

        commentWalkService.deleteAll(userId);

        return ApiResponse.success(204, "Delete all comment walks by user successfully", null);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCommentWalk(@PathVariable String id) {

        commentWalkService.delete(id);

        return ApiResponse.success(204, "Delete data comment walk successfully", null);
    }

}
