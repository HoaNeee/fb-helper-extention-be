package com.hoane.fbhelper.fbhelperextentionbe.controller;

import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.CommentPostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.entity.CommentPost;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.AuthService;
import com.hoane.fbhelper.fbhelperextentionbe.service.CommentPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment-posts")
@PreAuthorize("principal.enabled")
public class CommentPostController {

    @Autowired
    private CommentPostService commentPostService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse<CommentPost>> getCommentPostByUser() {

        String userId = authService.getUserIdFromContext();

        CommentPost commentPost = commentPostService.getCommentPostByUserOrThrow(userId);
        commentPost.setUser(null);
        return ApiResponse.success(200, "Comment post retrieved successfully", commentPost);
    }

    @GetMapping("/list-content")
    public ResponseEntity<ApiResponse<List<String>>> getListContent() {

        String userId = authService.getUserIdFromContext();

        CommentPost commentPost = commentPostService.getCommentPostByUserOrThrow(userId);
        return ApiResponse.success(200, "Comment post retrieved successfully", commentPost.getContents());
    }

    @GetMapping("/max-comment-per-post")
    public ResponseEntity<ApiResponse<Integer>> getMaxCommentPerPost() {

        String userId = authService.getUserIdFromContext();

        CommentPost commentPost = commentPostService.getCommentPostByUserOrThrow(userId);
        return ApiResponse.success(200, "Comment post retrieved successfully", commentPost.getMaxCommentPerPost());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentPost>> createCommentPostForUser() {

        String userId = authService.getUserIdFromContext();

        CommentPost commentPost = commentPostService.createNewCommentPostForUser(userId);
        commentPost.setUser(null);
        return ApiResponse.success(200, "Comment post created successfully", commentPost);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<CommentPost>> updateCommentPostByUser(@RequestBody CommentPostRequest commentPostRequest) {
        String userId = authService.getUserIdFromContext();

        CommentPost commentPost = commentPostService.updateCommentPostByUser(userId, commentPostRequest);
        commentPost.setUser(null);
        return ApiResponse.success(200, "Comment post updated successfully", commentPost);
    }
}
