package com.hoane.fbhelper.fbhelperextentionbe.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SchedulerTime;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.SchedulerType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SchedulerRequest {

    @NotNull(message = "Device id can not null or empty", groups = OnCreate.class)
    @Size(min = 8, message = "Device id must be at least 8 characters", groups = {OnUpdate.class, OnCreate.class})
    private String deviceId;

    @NotNull(message = "Scheduler type can not null or empty", groups = OnUpdate.class)
    private SchedulerType schedulerType;

    @Min(value = 1, message = "Scheduler time value must be greater than 0", groups = OnUpdate.class)
    private Integer schedulerTimeValue;

    private List<SchedulerTime> schedulerTimeList;
}
