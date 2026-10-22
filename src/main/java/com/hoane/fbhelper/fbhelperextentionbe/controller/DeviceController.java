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
import com.hoane.fbhelper.fbhelperextentionbe.service.DeviceService;
import com.hoane.fbhelper.fbhelperextentionbe.service.UserService;
import com.hoane.fbhelper.fbhelperextentionbe.utils.SecurityContextUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    @PreAuthorize("@userService.admin")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DeviceResponse>>> getAllDevicesByUser() {

        String user_id = SecurityContextUtils.getUserIdFromContext();

        List<DeviceResponse> devices = deviceService.findAllByUserWithDeviceSetting(user_id);

        return ApiResponse.success(200, "Get list devices successfully", devices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Device>> getDeviceDetail(@PathVariable String id) {

        Device device = deviceService.findByIdOrThrow(id);

        return ApiResponse.success(200, "Get detail device successfully", device);
    }

    @PreAuthorize("principal.enabled")
    @GetMapping("/get-all-data")
    public ResponseEntity<ApiResponse<Void>> getAllDataByDevice() {
        return ApiResponse.success(200, "Get all data of device successfully", null);
    }

    @PreAuthorize("principal.enabled")
    @GetMapping("/settings/{device_id}")
    public ResponseEntity<ApiResponse<DeviceSettingResponse>> getDeviceSettingOfDevice(@PathVariable String device_id) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        DeviceSettingResponse deviceSettingResponse = deviceService.getAllDataDeviceSetting(device_id, userId);

        mapSettingResponseForMember(deviceSettingResponse, userService.findById(userId));

        return ApiResponse.success(200, "Get device settings successfully", deviceSettingResponse);
    }

    @PreAuthorize("principal.enabled")
    @GetMapping("/settings/post-config/{device_id}")
    public ResponseEntity<ApiResponse<PostConfigResponse>> getPostConfigOfDevice(@PathVariable String device_id) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        PostConfig postConfig = deviceService.getOrCreatePostConfigByDeviceIdAndUserId(device_id, userId);

        return ApiResponse.success(200, "Get post config successfully", new PostConfigResponse(postConfig));
    }

    @PreAuthorize("@userService.member")
    @GetMapping("/settings/comment-walk-config/{device_id}")
    public ResponseEntity<ApiResponse<CommentWalkConfigResponse>> getCommentWalkConfigOfDevice(@PathVariable String device_id) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        CommentWalkConfig commentWalkConfig = deviceService.getOrCreateCommentWalkConfigByDeviceIdAndUserId(device_id, userId);

        return ApiResponse.success(200, "Get comment walk config successfully", new CommentWalkConfigResponse(commentWalkConfig));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<DeviceResponse>> createDevice(@Validated(OnCreate.class) @RequestBody DeviceRequest deviceRequest) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        Device device = deviceService.createNewDevice(deviceRequest, userId);

        return ApiResponse.success(201, "Create device successfully", new DeviceResponse(device));
    }

    @PreAuthorize("principal.enabled")
    @PostMapping("/settings")
    public ResponseEntity<ApiResponse<DeviceSettingResponse>> createDeviceSettings(DeviceSettingRequest deviceSettingRequest) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        String deviceId = deviceSettingRequest.getDeviceId();

        DeviceSetting deviceSetting = deviceService.createDeviceSetting(deviceId, userId);

        return ApiResponse.success(200, "Update device settings successfully", new DeviceSettingResponse(deviceSetting));
    }

    @PreAuthorize("principal.enabled")
    @PostMapping("/settings/post-config")
    public ResponseEntity<ApiResponse<PostConfigResponse>> createPostConfig(DeviceSettingRequest deviceSettingRequest) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        String deviceId = deviceSettingRequest.getDeviceId();

        PostConfig postConfig = deviceService.createNewPostConfig(deviceId, userId);

        return ApiResponse.success(200, "Update device settings successfully", new PostConfigResponse(postConfig));
    }

    @PreAuthorize("@userService.member")
    @PostMapping("/settings/comment-walk-config")
    public ResponseEntity<ApiResponse<CommentWalkConfigResponse>> createCommentWalkConfig(DeviceSettingRequest deviceSettingRequest) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        String deviceId = deviceSettingRequest.getDeviceId();

        CommentWalkConfig commentWalkConfig = deviceService.createNewCommentWalkConfig(deviceId, userId);

        return ApiResponse.success(200, "Update device settings successfully", new CommentWalkConfigResponse(commentWalkConfig));
    }

    @PreAuthorize("principal.enabled")
    @PostMapping("/sync-device-setting")
    public ResponseEntity<ApiResponse<DeviceSettingResponse>> syncDeviceData(@Valid @RequestBody RequestModels.DeviceSyncDataRequest syncDataRequest) {

        String userId = SecurityContextUtils.getUserIdFromContext();
        DeviceSettingResponse syncedDeviceSettingResponse = deviceService.syncDeviceSetting(userId, syncDataRequest);

        return ApiResponse.success(200, "Sync device data successfully", syncedDeviceSettingResponse);
    }

    @PreAuthorize("principal.enabled")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<DeviceResponse>> updateDevice(@PathVariable String id, @Validated(OnUpdate.class) @RequestBody DeviceRequest deviceRequest) {

        Device device = deviceService.updateDevice(id, deviceRequest);

        return ApiResponse.success(200, "Update device successfully", new DeviceResponse(device));
    }

    @PreAuthorize("@userService.admin")
    @PatchMapping("/update-status/{id}")
    public ResponseEntity<ApiResponse<Void>> updateDeviceStatus(@PathVariable String id, @Valid @RequestBody RequestModels.UpdateStatusDeviceRequest request) {

        deviceService.updateStatusDevice(id, request.status());

        return ApiResponse.success(200, "Update status device successfully", null);
    }

    @PreAuthorize("principal.enabled")
    @PatchMapping("/settings")
    public ResponseEntity<ApiResponse<DeviceSettingRequest>> updateDeviceSetting(@Valid @RequestBody DeviceSettingRequest deviceSettingRequest) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        deviceService.updateDeviceSetting(userId, deviceSettingRequest);

        return ApiResponse.success(200, "Update device settings successfully", deviceSettingRequest);
    }

    @PreAuthorize("principal.enabled")
    @PatchMapping("/settings/post-config")
    public ResponseEntity<ApiResponse<PostConfigResponse>> updatePostConfig(@Valid @RequestBody PostConfigRequest postConfigRequest) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        deviceService.updatePostConfig(userId, postConfigRequest);

        return ApiResponse.success(200, "Update post config successfully", new PostConfigResponse(postConfigRequest));
    }

    @PreAuthorize("@userService.member")
    @PatchMapping("/settings/comment-walk-config")
    public ResponseEntity<ApiResponse<CommentWalkConfigResponse>> updateCommentWalkConfig(@Valid @RequestBody CommentWalkConfigRequest commentWalkConfigRequest) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        deviceService.updateCommentWalkConfig(userId, commentWalkConfigRequest);

        return ApiResponse.success(200, "Update comment walk config successfully", new CommentWalkConfigResponse(commentWalkConfigRequest));
    }

    @PreAuthorize("principal.enabled")
    @PatchMapping("/settings/{device_id}/change-status-tool")
    public ResponseEntity<ApiResponse<PostConfigResponse>> changeStatusTool(@PathVariable String device_id, @Valid @RequestBody RequestModels.ChangeStatusTool request) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        System.out.println(request.toString());

        deviceService.changeStatusTool(userId, device_id, request);

        String msg = "Turn on tool successfully";

        if (request.isStopTask()) {
            msg = "Turn off tool successfully";
        }

        return ApiResponse.success(200, msg, null);

    }

    @PreAuthorize("@userService.member")
    @PatchMapping("/settings/{device_id}/update-priority-task")
    public ResponseEntity<ApiResponse<PriorityTask>> updatePriorityTask(@PathVariable String device_id, @Valid @RequestBody PriorityTask priorityTaskRequest) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        PriorityTask priorityTask = deviceService.updatePriorityTask(userId, device_id, priorityTaskRequest);

        return ApiResponse.success(200, "Update priority task success", priorityTask);

    }

    //maybe dont need
    @PreAuthorize("@userService.admin")
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

