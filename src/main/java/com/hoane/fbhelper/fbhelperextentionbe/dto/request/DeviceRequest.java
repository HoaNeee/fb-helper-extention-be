package com.hoane.fbhelper.fbhelperextentionbe.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.DeviceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DeviceRequest {

    @NotBlank(message = "Device id can not null or empty", groups = OnCreate.class)
    @Size(min = 8, message = "Device id must be at least 8 characters", groups = OnCreate.class)
    private String id;

    @Size(min = 1, message = "Device name can not null or empty", groups = OnUpdate.class)
    private String deviceName;

    @Size(min = 1, message = "Device type can not null or empty", groups = OnUpdate.class)
    private String deviceType;

    private DeviceStatus status;

}
