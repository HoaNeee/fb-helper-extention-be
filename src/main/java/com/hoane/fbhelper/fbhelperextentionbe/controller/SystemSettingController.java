package com.hoane.fbhelper.fbhelperextentionbe.controller;


import com.hoane.fbhelper.fbhelperextentionbe.entity.SystemCommentWalkSetting;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SystemPostSetting;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SystemSetting;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SystemToolSetting;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system-setting")
public class SystemSettingController {

    @Autowired
    private SystemSettingService systemSettingService;

    @GetMapping
    public ResponseEntity<ApiResponse<SystemSetting>> getSystemSetting() {

        SystemSetting systemSetting = systemSettingService.getSystemSetting();

        return ApiResponse.success(200, "Get system setting successfully", systemSetting);
    }

    @GetMapping("/tool")
    public ResponseEntity<ApiResponse<SystemToolSetting>> getSystemSettingTool() {
        return ApiResponse.success(200, "Get system setting tool successfully", systemSettingService.getSystemToolSetting());
    }

    @GetMapping("/post")
    public ResponseEntity<ApiResponse<SystemPostSetting>> getSystemSettingPost() {
        return ApiResponse.success(200, "Get system setting post successfully", systemSettingService.getSystemPostSetting());
    }

    @GetMapping("/comment-walk")
    public ResponseEntity<ApiResponse<SystemCommentWalkSetting>> getSystemSettingCommentWalk() {
        return ApiResponse.success(200, "Get system setting comment walk successfully", systemSettingService.getSystemCommentWalkSetting());
    }


    @PatchMapping
    @PreAuthorize("hasAllRoles('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<SystemSetting>> updateSystemSetting(@RequestBody SystemSetting systemSetting) {
        // Implement the logic to update system setting here
        systemSettingService.updateSystemSetting(systemSetting);
        return ApiResponse.success(200, "Update system setting successfully", null);
    }

    @PatchMapping("/tool")
    @PreAuthorize("hasAllRoles('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<SystemToolSetting>> updateSystemToolSetting(@RequestBody SystemToolSetting systemToolSetting) {
        // Implement the logic to update system setting here
        systemSettingService.updateSystemToolSetting(systemToolSetting);
        return ApiResponse.success(200, "Update system tool setting successfully", null);
    }

    @PatchMapping("/post")
    @PreAuthorize("hasAllRoles('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<SystemPostSetting>> updateSystemPostSetting(@RequestBody SystemPostSetting systemPostSetting) {
        // Implement the logic to update system setting here
        systemSettingService.updateSystemPostSetting(systemPostSetting);
        return ApiResponse.success(200, "Update system post setting successfully", null);
    }

    @PatchMapping("/comment-walk")
    @PreAuthorize("hasAllRoles('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<SystemCommentWalkSetting>> updateSystemCommentWalkSetting(@RequestBody SystemCommentWalkSetting systemCommentWalkSetting) {
        // Implement the logic to update system setting here
        systemSettingService.updateSystemCommentWalkSetting(systemCommentWalkSetting);
        return ApiResponse.success(200, "Update system comment walk setting successfully", null);
    }
}
