package com.hoane.fbhelper.fbhelperextentionbe.dto.request;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SchedulerRequest {

    @NotNull(message = "Device id can not null or empty", groups = OnCreate.class)
    @Size(min = 1, message = "Device id can not null or empty", groups = OnUpdate.class)
    private String device_id;

    @NotNull(message = "Scheduler type can not null or empty", groups = OnCreate.class)
    @Size(min = 1, message = "Scheduler type can not null or empty", groups = OnUpdate.class)
    private String scheduler_type;
}
