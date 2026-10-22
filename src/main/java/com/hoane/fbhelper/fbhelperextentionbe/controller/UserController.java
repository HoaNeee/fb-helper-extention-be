package com.hoane.fbhelper.fbhelperextentionbe.controller;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.UserRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.UserDetailsResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.AuthService;
import com.hoane.fbhelper.fbhelperextentionbe.service.UserService;
import com.hoane.fbhelper.fbhelperextentionbe.utils.SecurityContextUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        String userId = SecurityContextUtils.getUserIdFromContext();

        User user = userService.findByIdOrThrow(userId);
        UserDetailsResponse userDetailsResponse = UserDetailsResponse
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .avatar(user.getAvatar())
                .name(user.getName())
                .isPremium(user.isMember())
                .status(user.getStatus())
                .build();

        if (user.getIsNewUser()) {
            authService.handleForNewUser(userId);
        }

        if (user.isRole(Role.ROLE_ADMIN) || user.isRole(Role.ROLE_SUPER_ADMIN)) {
            userDetailsResponse.setRole(user.getRole());
        }

        return ApiResponse.success(200, "User details retrieved", userDetailsResponse);
    }


    @GetMapping("/is-premium")
    public ResponseEntity<ApiResponse<Boolean>> getIsPremium() {
        String userId = SecurityContextUtils.getUserIdFromContext();
        User user = userService.findByIdOrThrow(userId);

        return ApiResponse.success(200, "User premium status retrieved", user.isMember());
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> updateLoggedInUser(@Valid @RequestBody UserRequest.UserUpdateRequest userUpdateRequest) {
        String userId = SecurityContextUtils.getUserIdFromContext();

        User user = userService.update(userId, userUpdateRequest);

        return ApiResponse.success(200, "User details updated", new UserDetailsResponse(user));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @PatchMapping("/change-role")
    public ResponseEntity<ApiResponse<String>> changeRole(@Valid @RequestBody UserRequest.UserUpdateRoleRequest userUpdateRoleRequest) {

        String role = userService.updateRole(userUpdateRoleRequest);
        return ApiResponse.success(200, "Change user role success", role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @PatchMapping("/change-status")
    public ResponseEntity<ApiResponse<String>> changeStatus(@Valid @RequestBody UserRequest.UserUpdateStatusRequest userUpdateStatusRequest) {

        String status = userService.updateStatus(userUpdateStatusRequest);

        return ApiResponse.success(200, "Change status for user success", status);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody UserRequest.UserChangePasswordRequest userChangePasswordRequest) {
        String userId = SecurityContextUtils.getUserIdFromContext();

        userService.changePassword(userId, userChangePasswordRequest);

        return ApiResponse.success(200, "Change password success", null);
    }

}
