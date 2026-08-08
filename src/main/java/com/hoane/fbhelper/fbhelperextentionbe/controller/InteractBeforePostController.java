package com.hoane.fbhelper.fbhelperextentionbe.controller;


import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.CommentPostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.InteractBeforePostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.entity.CommentPost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.InteractBeforePost;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.AuthService;
import com.hoane.fbhelper.fbhelperextentionbe.service.InteractBeforePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interact-before-posts")
@PreAuthorize("principal.enabled")
public class InteractBeforePostController {

    @Autowired
    private InteractBeforePostService interactBeforePostService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse<InteractBeforePost>> getInteractBeforePostByUser() {

        String userId = authService.getUserIdFromContext();

        InteractBeforePost interactBeforePost = interactBeforePostService.getInteractPostByUserOrThrow(userId);
        interactBeforePost.setUser(null);
        return ApiResponse.success(200, "Interact before post retrieved successfully", interactBeforePost);
    }

    @GetMapping("/max-post-interact-per-batch")
    public ResponseEntity<ApiResponse<Integer>> getMaxPostInteractPerBatch() {

        String userId = authService.getUserIdFromContext();

        InteractBeforePost interactBeforePost = interactBeforePostService.getInteractPostByUserOrThrow(userId);
        interactBeforePost.setUser(null);
        return ApiResponse.success(200, "Interact before post retrieved successfully", interactBeforePost.getMaxPostInteractPerBatch());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InteractBeforePost>> createInteractBeforePostForUser() {

        String userId = authService.getUserIdFromContext();

        InteractBeforePost interactBeforePost = interactBeforePostService.createInteractBeforePostForUser(userId);
        interactBeforePost.setUser(null);
        return ApiResponse.success(200, "Interact before post created successfully", interactBeforePost);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<InteractBeforePost>> updateCommentPostByUser(@RequestBody InteractBeforePostRequest interactBeforePostRequest) {

        String userId = authService.getUserIdFromContext();

        InteractBeforePost interactBeforePost = interactBeforePostService.updateInteractBeforePostByUser(userId, interactBeforePostRequest);
        interactBeforePost.setUser(null);
        return ApiResponse.success(200, "Comment post updated successfully", interactBeforePost);
    }

}
