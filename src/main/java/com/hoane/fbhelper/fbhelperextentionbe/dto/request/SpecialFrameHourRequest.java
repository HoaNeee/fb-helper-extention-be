package com.hoane.fbhelper.fbhelperextentionbe.dto.request;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

public class SpecialFrameHourRequest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SpecialRequest {

        @NotNull(message = "start_time is required", groups = OnCreate.class)
        @Min(value = 0, message = "start_time must be greater than or equal to 0", groups = {OnCreate.class, OnUpdate.class})
        @Max(value = 23, message = "start_time must be less than or equal to 23", groups = {OnCreate.class, OnUpdate.class})
        private Integer startTime;

        @NotNull(message = "end_time is required", groups = OnCreate.class)
        @Min(value = 0, message = "end_time must be greater than or equal to 0", groups = {OnCreate.class, OnUpdate.class})
        @Max(value = 23, message = "end_time must be less than or equal to 23", groups = {OnCreate.class, OnUpdate.class})
        private Integer endTime;

        @NotNull(message = "max_group is required", groups = OnCreate.class)
        @Min(value = 1, message = "max_group must be greater than or equal", groups = {OnCreate.class, OnUpdate.class})
        private Integer maxGroup;

        @NotNull(message = "apply_dates is required", groups = OnCreate.class)
        private List<Integer> applyDates;

        @NotNull(message = "device_id is required", groups = OnCreate.class)
        @Size(min = 8, message = "device_id must be at least 8 characters", groups = OnCreate.class)
        private String deviceId;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SpecialFrameSettingRequest {
        @NotBlank(message = "device_id is required")
        @Size(min = 8, message = "device_id must be at least 8 characters")
        private String deviceId;

        @NotNull(message = "is_active is required")
        private Boolean isActive;
    }
}
