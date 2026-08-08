package com.hoane.fbhelper.fbhelperextentionbe.controller;

import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.SpecialFrameHourRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.SpecialFrameHourResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SpecialFrameHour;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.AuthService;
import com.hoane.fbhelper.fbhelperextentionbe.service.SpecialFrameHourService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/special-frame-hours")
@PreAuthorize("@authSecurityService.isMember()")
public class SpecialFrameHourController {
    @Autowired
    private SpecialFrameHourService specialFrameHourService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SpecialFrameHour>>> getSpecialFrameHoursByUser() {

        String user_id = authService.getUserIdFromContext();

        List<SpecialFrameHour> specialFrameHours = specialFrameHourService.findAllSpecialFrameHoursByUserId(user_id);
        return ApiResponse.success(200, "Get list special frame hours success", specialFrameHours);
    }

    @GetMapping("/{device_id}")
    public ResponseEntity<ApiResponse<List<SpecialFrameHourResponse>>> getSpecialFrameHoursByDevice(@PathVariable String device_id) {

        String user_id = authService.getUserIdFromContext();

        List<SpecialFrameHourResponse> specialFrameHours = specialFrameHourService.findAllSpecialFrameHoursByDeviceIdAndUserId(user_id, device_id);

        return ApiResponse.success(200, "Get list special frame hours by device success", specialFrameHours);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SpecialFrameHourResponse>> createSpecialFrameHour(@Validated(OnCreate.class) @RequestBody SpecialFrameHourRequest.SpecialRequest specialFrameRequest) {

        String user_id = authService.getUserIdFromContext();

        SpecialFrameHour specialFrameHour = specialFrameHourService.createSpecialFrameHour(user_id, specialFrameRequest);

        return ApiResponse.success(200, "Create special frame hour success", new SpecialFrameHourResponse(specialFrameHour));
    }

    @PatchMapping("/{special_frame_hour_id}")
    public ResponseEntity<ApiResponse<SpecialFrameHourResponse>> updateSpecialFrameHour(@PathVariable int special_frame_hour_id, @Validated(OnUpdate.class) @RequestBody SpecialFrameHourRequest.SpecialRequest specialFrameRequest) {

        SpecialFrameHour specialFrameHour = specialFrameHourService.updateSpecialFrameHour(special_frame_hour_id, specialFrameRequest);

        return ApiResponse.success(200, "Update special frame hour success", new SpecialFrameHourResponse(specialFrameHour));
    }

    @PatchMapping("/update-status/{special_frame_hour_id}")
    public ResponseEntity<ApiResponse<Void>> updateSpecialFrameHourStatus(@PathVariable int special_frame_hour_id, @Valid @RequestBody SpecialFrameHourRequest.SpecialFrameSettingRequest specialFrameSettingRequest) {

        specialFrameHourService.updateStatusSpecialFrameHour(special_frame_hour_id, specialFrameSettingRequest);

        return ApiResponse.success(200, "Update special frame hour status success", null);
    }

    @DeleteMapping("/{special_frame_hour_id}")
    public ResponseEntity<ApiResponse<Void>> deleteSpecialFrameHour(@PathVariable int special_frame_hour_id) {

        specialFrameHourService.deleteSpecialFrameHour(special_frame_hour_id);

        return ApiResponse.success(200, "Delete special frame hour success", null);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<ApiResponse<Void>> deleteAllSpecialFrameHours() {

        String userId = authService.getUserIdFromContext();

        specialFrameHourService.clearAllSpecialFrameHour(userId);
        return ApiResponse.success(200, "Delete all special frame hours success", null);
    }
}
