package com.hoane.fbhelper.fbhelperextentionbe.controller;


import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DeviceRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DeviceSettingRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RequestModels;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DeviceResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Device;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DeviceSetting;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
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
    public ResponseEntity<ApiResponse<DeviceSetting>> getDeviceSettingOfDevice(@PathVariable String device_id) {

        String userId = authService.getUserIdFromContext();

        DeviceSetting deviceSetting = deviceService.findDeviceSettingByDeviceIdAndUserId(device_id, userId);

        deviceSetting.setDevice(null);
        deviceSetting.setUser(null);
        mapSettingForMember(deviceSetting, userService.findByIdOrThrow(userId));

        return ApiResponse.success(200, "Get device settings successfully", deviceSetting);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeviceResponse>> createDevice(@Validated(OnCreate.class) @RequestBody DeviceRequest deviceRequest) {

        String userId = authService.getUserIdFromContext();

        Device device = deviceService.createNewDevice(userId, deviceRequest);

        return ApiResponse.success(201, "Create device successfully", new DeviceResponse(device));
    }

    @PostMapping("/settings")
    public ResponseEntity<ApiResponse<DeviceSetting>> createDeviceSettings(DeviceSettingRequest deviceSettingRequest) {

        String userId = authService.getUserIdFromContext();

        DeviceSetting deviceSetting = deviceService.createDeviceSetting(deviceSettingRequest, userId);

        return ApiResponse.success(200, "Update device settings successfully", deviceSetting);
    }

    @PostMapping("/sync-data")
    public ResponseEntity<ApiResponse<Void>> syncDeviceData(@Valid @RequestBody RequestModels.DeviceSyncDataRequest syncDataRequest) {
        return ApiResponse.success(200, "Sync device data successfully", null);
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

    //maybe dont need
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDevice(@PathVariable String id) {
        deviceService.deleteDevice(id);
        return ApiResponse.success(200, "Delete device successfully", null);
    }


    private void mapSettingForMember(DeviceSetting deviceSetting, User user) {

        if (!user.isMember()) {
            deviceSetting.setIsRandomBreakBatch(null);
            deviceSetting.setIsSpecialFrameHours(null);
            deviceSetting.setIsRandomTimePost(null);
            deviceSetting.setIsFixStealAllFocus(null);
        }
    }

}

