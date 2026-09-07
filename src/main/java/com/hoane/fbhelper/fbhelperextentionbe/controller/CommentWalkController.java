package com.hoane.fbhelper.fbhelperextentionbe.controller;


import com.hoane.fbhelper.fbhelperextentionbe.dto.request.CommentWalkRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DataGroupPostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.CommentWalkResponse;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DataGroupPostResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.CommentWalk;
import com.hoane.fbhelper.fbhelperextentionbe.entity.CommentWalkDetail;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPostDetail;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.AuthService;
import com.hoane.fbhelper.fbhelperextentionbe.service.CommentWalkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment-walks")
@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
public class CommentWalkController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CommentWalkService commentWalkService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentWalk>>> getCommentWalks() {

        String userId = authService.getUserIdFromContext();

        List<CommentWalk> res = commentWalkService.findAllByUser(userId).stream().peek(commentWalk -> commentWalk.setUser(null)).toList();

        return ApiResponse.success(200, "Get list data comment walks successfully", res);
    }

    @GetMapping("/device/{device_id}")
    public ResponseEntity<ApiResponse<List<CommentWalkResponse.DataResponse>>> getCommentWalkByDevice(@PathVariable String device_id) {

        String userId = authService.getUserIdFromContext();

        List<CommentWalkResponse.DataResponse> list = commentWalkService.getListCommentWalkAndDetails(userId, device_id);

        return ApiResponse.success(200, "Get list data comment walk for device successfully", list);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentWalkResponse.DataResponse>> createCommentWalk(@Validated(OnCreate.class) @RequestBody CommentWalkRequest.BaseRequest request) {

        String user_id = authService.getUserIdFromContext();

        CommentWalk commentWalk = commentWalkService.createCommentWalk(request, user_id);

        return ApiResponse.success(201, "Create comment walk successfully", new CommentWalkResponse.DataResponse(commentWalk));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentWalkResponse.DataResponse>> updateCommentWalk(@PathVariable String id, @Validated(OnUpdate.class) @RequestBody CommentWalkRequest.BaseRequest request) {

        CommentWalkResponse.DataResponse commentWalk = commentWalkService.updateCommentWalk(id, request);

        return ApiResponse.success(200, "Update comment walk successfully", commentWalk);
    }

    @PatchMapping("/details")
    public ResponseEntity<ApiResponse<CommentWalkResponse.CommentWalkDetailResponse>> updateCommentWalkDetail(@Valid @RequestBody CommentWalkRequest.CommentWalkDetailRequest request) {

        CommentWalkDetail commentWalkDetail = commentWalkService.updateCommentWalkDetail(request);

        return ApiResponse.success(200, "Update comment walk status successfully", new CommentWalkResponse.CommentWalkDetailResponse(commentWalkDetail));
    }

    @DeleteMapping("/delete-all-comment-walks")
    public ResponseEntity<ApiResponse<Void>> deleteAllCommentWalksByUser() {

        String userId = authService.getUserIdFromContext();

        commentWalkService.deleteAll(userId);

        return ApiResponse.success(200, "Delete all comment walks by user successfully", null);
    }

}
