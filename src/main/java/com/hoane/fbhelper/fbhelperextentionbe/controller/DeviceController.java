package com.hoane.fbhelper.fbhelperextentionbe.controller;


import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DeviceRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DeviceResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Device;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {


    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeviceResponse>>> getDevices() {

        int user_id = Constant.USER_ID;

        List<DeviceResponse> devices = deviceService.findAllByUser(user_id).stream()
                .map(DeviceResponse::new)
                .toList();

        return ApiResponse.success(200, "Get list devices successfully", devices);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeviceResponse>> createDevice(@Validated(OnCreate.class) @RequestBody DeviceRequest deviceRequest) {

        int user_id = Constant.USER_ID;

        Device device = deviceService.create(deviceRequest, user_id);

        return ApiResponse.success(201, "Create device successfully", new DeviceResponse(device));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<DeviceResponse>> updateDevice(@PathVariable String id, @Validated(OnUpdate.class) @RequestBody DeviceRequest deviceRequest) {
        Device device = deviceService.update(id, deviceRequest);
        return ApiResponse.success(200, "Update device successfully", new DeviceResponse(device));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDevice(@PathVariable String id) {
        deviceService.delete(id);
        return ApiResponse.success(200, "Delete device successfully", null);
    }

}
