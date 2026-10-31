package com.hoane.fbhelper.fbhelperextentionbe.dto.request;

import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.DeviceSettingStatus;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.DeviceStatus;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.IpStatus;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.SchedulerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.Map;

public class RequestModels {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record DeviceSyncDataRequest(
            @NotBlank(message = "Current device id can not null or empty") @Size(min = 8, message = "Current device id must be at least 8 characters") String currentDeviceId,
            @NotBlank(message = "Device id sync not null or empty") @Size(min = 8, message = "Device id sync must be at least 8 characters") String targetDeviceId
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record SchedulerChangeTypeRequest(
            @NotNull(message = "Scheduler type can not null or empty") SchedulerType schedulerType
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record DataGroupPostImportRequest(
            List<DataGroupPostRequest.DataGroupRequest> listDataGroupPost,
            String deviceId
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record CommentWalkImportRequest(
            List<CommentWalkRequest.BaseRequest> listCommentWalk,
            String deviceId
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ChangeStatusTool(
            @NotNull(message = "Is stop task can not null or empty") Boolean isStopTask,
            String requestPlatform
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record VerifyTokenRequest(
            @NotBlank(message = "Token can not null or empty") String token
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record UpdateStatusIpAddressRequest(
            @NotBlank(message = "IP address can not null or empty") String ipAddress,
            @NotNull(message = "Status can not null or empty") IpStatus status
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record UpdateStatusDeviceRequest(
            @NotBlank(message = "Status can not null or empty") String status
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record UpdateStatusDeviceSettingRequest(
            @NotBlank(message = "Status can not null or empty") String status,
            @NotBlank(message = "Device ID can not null or empty") String deviceId,
            @NotBlank(message = "User ID can not null or empty") String userId
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record UpdateIsChangedRequest(
            @NotNull(message = "Changed can not null") Boolean changed
    ) {
    }

}
