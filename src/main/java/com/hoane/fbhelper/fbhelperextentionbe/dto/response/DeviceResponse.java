package com.hoane.fbhelper.fbhelperextentionbe.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Device;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DeviceResponse {
    private String id;

    private String deviceName;

    private String deviceType;
    
    private DeviceSettingResponse deviceSetting;

    private DeviceStatus status;

    private LocalDateTime createdAt;

    public DeviceResponse(Device device) {
        this.id = device.getId();
        this.deviceName = device.getDeviceName();
        this.deviceType = device.getDeviceType();
        this.createdAt = device.getCreatedAt();
        this.status = device.getStatus();
    }
}
