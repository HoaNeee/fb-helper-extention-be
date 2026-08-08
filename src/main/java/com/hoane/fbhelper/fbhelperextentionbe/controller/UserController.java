package com.hoane.fbhelper.fbhelperextentionbe.controller;

import com.hoane.fbhelper.fbhelperextentionbe.dto.response.UserDetailsResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.AuthService;
import com.hoane.fbhelper.fbhelperextentionbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@PreAuthorize("principal.enabled")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getLoggedInUser() {
        String userId = authService.getUserIdFromContext();

        User user = userService.findByIdOrThrow(userId);
        UserDetailsResponse userDetailsResponse = UserDetailsResponse
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .role(user.getRole())
                .avatar(user.getAvatar())
                .name(user.getName())
                .status(user.getStatus())
                .build();

        if (user.getIsNewUser()) {
            authService.handleForNewUser(userId);
        }

        return ApiResponse.success(200, "User details retrieved", userDetailsResponse);
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> checkUser() {
        return ApiResponse.success(200, "Check user successful", true);
    }

    @GetMapping("/is-premium")
    public ResponseEntity<ApiResponse<Boolean>> getIsPremium() {
        String userId = authService.getUserIdFromContext();
        User user = userService.findByIdOrThrow(userId);
        
        return ApiResponse.success(200, "User premium status retrieved", user.isMember());
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> updateLoggedInUser() {
        return ApiResponse.success(200, "User details updated", null);
    }

    @PatchMapping("/change-role")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> changeRole() {
        return ApiResponse.success(200, "User details updated", null);
    }

    @PatchMapping("/change-status")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> changeStatus() {
        return ApiResponse.success(200, "User details updated", null);
    }

}
