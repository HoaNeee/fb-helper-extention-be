package com.hoane.fbhelper.fbhelperextentionbe.dto.request;

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
            @NotBlank(message = "Device id sync not null or empty") @Size(min = 8, message = "Device id sync must be at least 8 characters") String deviceSyncId
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
}
