package com.hoane.fbhelper.fbhelperextentionbe.controller;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RequestModels;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.SchedulerRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.SchedulerResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Scheduler;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SchedulerDetail;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SchedulerTime;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.SchedulerType;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.SchedulerService;
import com.hoane.fbhelper.fbhelperextentionbe.utils.SecurityContextUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedulers")
@PreAuthorize("principal.enabled")
public class SchedulerController {

    @Autowired
    private SchedulerService schedulerService;


    @GetMapping("/{device_id}")
    public ResponseEntity<ApiResponse<SchedulerResponse.SchedulerDeviceResponse>> getSchedulerByDeviceId(@PathVariable String device_id) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        Scheduler scheduler = schedulerService.getSchedulerByDeviceIdAndUserId(device_id, userId);

        SchedulerResponse.SchedulerDeviceResponse schedulerDeviceResponse = new SchedulerResponse.SchedulerDeviceResponse(scheduler);

        return ApiResponse.success(200, "Get scheduler successfully", schedulerDeviceResponse);
    }

    @GetMapping("/details/{schedulerType}/{deviceId}")
    public ResponseEntity<ApiResponse<SchedulerDetail>> getSchedulerDetailByTypeAndDeviceId(@PathVariable SchedulerType schedulerType, @PathVariable String deviceId, @RequestParam(required = false) Map<String, String> params) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        Scheduler scheduler = schedulerService.findSchedulerByDeviceIdAndUserIdOrThrow(deviceId, userId);

        SchedulerDetail schedulerDetail = schedulerService.findSchedulerDetailBySchedulerIdAndType(scheduler.getId(), schedulerType);
        if (schedulerDetail != null) {
            schedulerDetail.setScheduler(null);
        }
        return ApiResponse.success(200, "Get scheduler detail successfully", schedulerDetail);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Scheduler>> createNewScheduler(@Validated(OnCreate.class) @RequestBody SchedulerRequest schedulerRequest) {

        String userId = SecurityContextUtils.getUserIdFromContext();

        Scheduler scheduler = schedulerService.createNewScheduler(schedulerRequest, userId);

        return ApiResponse.success(201, "Create new scheduler successfully", scheduler);
    }

    @PatchMapping("/change-type/{deviceId}")
    public ResponseEntity<ApiResponse<SchedulerDetail>> updateTypeScheduler(@PathVariable String deviceId, @Valid @RequestBody RequestModels.SchedulerChangeTypeRequest schedulerChangeTypeRequest) {
        String userId = SecurityContextUtils.getUserIdFromContext();
        SchedulerType type = schedulerChangeTypeRequest.schedulerType();
        SchedulerDetail schedulerDetail = schedulerService.changeTypeScheduler(deviceId, userId, type);
        schedulerDetail.setScheduler(null);
        return ApiResponse.success(200, "Change scheduler type successfully", schedulerDetail);
    }

    @PatchMapping("/update/{deviceId}")
    public ResponseEntity<ApiResponse<SchedulerDetail>> updateScheduler(@PathVariable String deviceId, @Validated(OnUpdate.class) @RequestBody SchedulerRequest schedulerRequest) {
        String userId = SecurityContextUtils.getUserIdFromContext();
        SchedulerDetail schedulerDetail = schedulerService.updateScheduler(deviceId, userId, schedulerRequest);
        schedulerDetail.setScheduler(null);

        return ApiResponse.success(200, "Update scheduler successfully", schedulerDetail);
    }
}
