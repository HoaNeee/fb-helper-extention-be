package com.hoane.fbhelper.fbhelperextentionbe.controller;


import com.hoane.fbhelper.fbhelperextentionbe.dto.request.*;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.CommentWalkConfigResponse;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DeviceResponse;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DeviceSettingResponse;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.PostConfigResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.*;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.AuthService;
import com.hoane.fbhelper.fbhelperextentionbe.service.DeviceService;
import com.hoane.fbhelper.fbhelperextentionbe.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
@PreAuthorize("principal.enabled")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeviceResponse>>> getAllDevicesByUser() {

        String user_id = authService.getUserIdFromContext();

        List<DeviceResponse> devices = deviceService.findAllByUser(user_id).stream()
                .map(DeviceResponse::new)
                .toList();

        return ApiResponse.success(200, "Get list devices successfully", devices);
    }

    @GetMapping("/get-all-data")
    public ResponseEntity<ApiResponse<Void>> getAllDataByDevice() {
        return ApiResponse.success(200, "Get all data of device successfully", null);
    }


    @GetMapping("/settings/{device_id}")
    public ResponseEntity<ApiResponse<DeviceSettingResponse>> getDeviceSettingOfDevice(@PathVariable String device_id) {

        String userId = authService.getUserIdFromContext();

        DeviceSettingResponse deviceSettingResponse = deviceService.getAllDataDeviceSetting(device_id, userId);

        mapSettingResponseForMember(deviceSettingResponse, userService.findById(userId));

        return ApiResponse.success(200, "Get device settings successfully", deviceSettingResponse);
    }

    @GetMapping("/settings/post-config/{device_id}")
    public ResponseEntity<ApiResponse<PostConfigResponse>> getPostConfigOfDevice(@PathVariable String device_id) {

        String userId = authService.getUserIdFromContext();

        PostConfig postConfig = deviceService.findPostConfigByDeviceIdAndUserId(device_id, userId);

        return ApiResponse.success(200, "Get post config successfully", new PostConfigResponse(postConfig));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    @GetMapping("/settings/comment-walk-config/{device_id}")
    public ResponseEntity<ApiResponse<CommentWalkConfigResponse>> getCommentWalkConfigOfDevice(@PathVariable String device_id) {

        String userId = authService.getUserIdFromContext();

        CommentWalkConfig commentWalkConfig = deviceService.findCommentWalkConfigByDeviceIdAndUserId(device_id, userId);

        return ApiResponse.success(200, "Get comment walk config successfully", new CommentWalkConfigResponse(commentWalkConfig));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeviceResponse>> createDevice(@Validated(OnCreate.class) @RequestBody DeviceRequest deviceRequest) {

        String userId = authService.getUserIdFromContext();

        Device device = deviceService.createNewDevice(userId, deviceRequest);

        return ApiResponse.success(201, "Create device successfully", new DeviceResponse(device));
    }

    @PostMapping("/settings")
    public ResponseEntity<ApiResponse<DeviceSettingResponse>> createDeviceSettings(DeviceSettingRequest deviceSettingRequest) {

        String userId = authService.getUserIdFromContext();

        String deviceId = deviceSettingRequest.getDeviceId();

        DeviceSetting deviceSetting = deviceService.createDeviceSetting(deviceId, userId);

        return ApiResponse.success(200, "Update device settings successfully", new DeviceSettingResponse(deviceSetting));
    }

    @PostMapping("/settings/post-config")
    public ResponseEntity<ApiResponse<PostConfigResponse>> createPostConfig(DeviceSettingRequest deviceSettingRequest) {

        String userId = authService.getUserIdFromContext();

        String deviceId = deviceSettingRequest.getDeviceId();

        PostConfig postConfig = deviceService.createNewPostConfig(deviceId, userId);

        return ApiResponse.success(200, "Update device settings successfully", new PostConfigResponse(postConfig));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    @PostMapping("/settings/comment-walk-config")
    public ResponseEntity<ApiResponse<CommentWalkConfigResponse>> createCommentWalkConfig(DeviceSettingRequest deviceSettingRequest) {

        String userId = authService.getUserIdFromContext();

        String deviceId = deviceSettingRequest.getDeviceId();

        CommentWalkConfig commentWalkConfig = deviceService.createNewCommentWalkConfig(deviceId, userId);

        return ApiResponse.success(200, "Update device settings successfully", new CommentWalkConfigResponse(commentWalkConfig));
    }

    @PostMapping("/sync-device-setting")
    public ResponseEntity<ApiResponse<DeviceSetting>> syncDeviceData(@Valid @RequestBody RequestModels.DeviceSyncDataRequest syncDataRequest) {

        String userId = authService.getUserIdFromContext();
        DeviceSetting syncedDeviceSetting = deviceService.syncDeviceSetting(userId, syncDataRequest);
        syncedDeviceSetting.setDevice(null);
        syncedDeviceSetting.setUser(null);

        return ApiResponse.success(200, "Sync device data successfully", syncedDeviceSetting);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<DeviceResponse>> updateDevice(@PathVariable String id, @Validated(OnUpdate.class) @RequestBody DeviceRequest deviceRequest) {

        Device device = deviceService.updateDevice(id, deviceRequest);

        return ApiResponse.success(200, "Update device successfully", new DeviceResponse(device));
    }

    @PatchMapping("/settings")
    public ResponseEntity<ApiResponse<DeviceSettingRequest>> updateDeviceSetting(@Valid @RequestBody DeviceSettingRequest deviceSettingRequest) {

        String userId = authService.getUserIdFromContext();

        deviceService.updateDeviceSetting(userId, deviceSettingRequest);

        return ApiResponse.success(200, "Update device settings successfully", deviceSettingRequest);
    }

    @PatchMapping("/settings/post-config")
    public ResponseEntity<ApiResponse<PostConfigResponse>> updatePostConfig(@Valid @RequestBody PostConfigRequest postConfigRequest) {

        String userId = authService.getUserIdFromContext();

        deviceService.updatePostConfig(userId, postConfigRequest);

        return ApiResponse.success(200, "Update post config successfully", new PostConfigResponse(postConfigRequest));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    @PatchMapping("/settings/comment-walk-config")
    public ResponseEntity<ApiResponse<CommentWalkConfigResponse>> updateCommentWalkConfig(@Valid @RequestBody CommentWalkConfigRequest commentWalkConfigRequest) {

        String userId = authService.getUserIdFromContext();

        deviceService.updateCommentWalkConfig(userId, commentWalkConfigRequest);

        return ApiResponse.success(200, "Update comment walk config successfully", new CommentWalkConfigResponse(commentWalkConfigRequest));
    }

    @PatchMapping("/settings/{device_id}/change-status-tool")
    public ResponseEntity<ApiResponse<PostConfigResponse>> changeStatusTool(@PathVariable String device_id, @Valid @RequestBody RequestModels.ChangeStatusTool request) {

        String userId = authService.getUserIdFromContext();

        System.out.println(request.toString());

        deviceService.changeStatusTool(userId, device_id, request);

        String msg = "Turn on tool successfully";

        if (request.isStopTask()) {
            msg = "Turn off tool successfully";
        }

        return ApiResponse.success(200, msg, null);

    }

    //maybe dont need
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDevice(@PathVariable String id) {
        deviceService.deleteDevice(id);
        return ApiResponse.success(200, "Delete device successfully", null);
    }


    private void mapSettingResponseForMember(DeviceSettingResponse response, User user) {
        if (!user.isMember()) {
            response.setIsFixStealAllFocus(null);
            response.setCommentWalkConfig(null);
            response.setIsRandomBreakBatch(null);
            response.setIsSpecialFrameHours(null);

        }

    }

}

