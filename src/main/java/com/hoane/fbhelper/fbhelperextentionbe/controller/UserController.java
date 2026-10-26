package com.hoane.fbhelper.fbhelperextentionbe.controller;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.BasePageRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.UserRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.PageResponse;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.ResponseModels;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.UserDetailsResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Pagination;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.AuthService;
import com.hoane.fbhelper.fbhelperextentionbe.service.UserService;
import com.hoane.fbhelper.fbhelperextentionbe.utils.SecurityContextUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody UserRequest.UserChangePasswordRequest userChangePasswordRequest) {
        String userId = SecurityContextUtils.getUserIdFromContext();

        userService.changePassword(userId, userChangePasswordRequest);

        return ApiResponse.success(200, "Change password success", null);
    }

    @PreAuthorize("@userService.admin")
    @GetMapping
    public ResponseEntity<PageResponse<List<UserDetailsResponse>>> getALlUser(@Valid @ModelAttribute BasePageRequest basePageRequest) {

        Page<User> users = userService.getAllUser(basePageRequest.toPageable());

        Pagination pagination = new Pagination(users.getNumber(), users.getSize(), users.getTotalElements(), users.getTotalPages(), users.hasNext());

        List<UserDetailsResponse> userDetailsResponses = users.getContent().stream().map(u ->
                new UserDetailsResponse(u, u.getRole())
        ).toList();

        return ResponseEntity.ok(new PageResponse<>(200, "Get all user success", true, userDetailsResponses, pagination));
    }

    @GetMapping("/admin/statistics")
    public ResponseEntity<ApiResponse<ResponseModels.AdminStatisticUserResponse>> getAdminStatistics() {
        ResponseModels.AdminStatisticUserResponse statistics = userService.getAdminStatistics();
        return ApiResponse.success(200, "Admin statistics retrieved", statistics);
    }

    @PreAuthorize("@userService.admin")
    @PostMapping("/admin/create-user")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> adminCreateNewUser(@Valid @RequestBody UserRequest.CreateUserRequest request) {
        User u = userService.create(request);
        return ApiResponse.success(200, "Create user by admin success", new UserDetailsResponse(u));
    }

    @PreAuthorize("@userService.admin")
    @PatchMapping("/admin/update-user/{userId}")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> adminUpdateUser(@Valid @RequestBody UserRequest.AdminUpdateUserRequest request, @PathVariable String userId) {
        User user = userService.adminUpdate(userId, request);
        return ApiResponse.success(200, "update user by admin success", new UserDetailsResponse(user, user.getRole()));
    }


    @PreAuthorize("@userService.admin")
    @PatchMapping("/admin/change-role")
    public ResponseEntity<ApiResponse<String>> changeRole(@Valid @RequestBody UserRequest.UserUpdateRoleRequest userUpdateRoleRequest) {

        String role = userService.updateRole(userUpdateRoleRequest);
        return ApiResponse.success(200, "Change user role success", role);
    }

    @PreAuthorize("@userService.admin")
    @PatchMapping("/admin/change-status")
    public ResponseEntity<ApiResponse<String>> changeStatus(@Valid @RequestBody UserRequest.UserUpdateStatusRequest userUpdateStatusRequest) {

        String status = userService.updateStatus(userUpdateStatusRequest);

        return ApiResponse.success(200, "Change status for user success", status);
    }

    @PreAuthorize("@userService.admin")
    @PatchMapping("/admin/delete-user/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> adminDeleteUser(@PathVariable String userId) {

        Boolean success = userService.deleteUser(userId);

        return ApiResponse.success(200, "Delete user success", success);
    }


}
