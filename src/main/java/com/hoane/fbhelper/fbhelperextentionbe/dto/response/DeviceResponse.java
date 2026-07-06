package com.hoane.fbhelper.fbhelperextentionbe.dto.response;


import com.hoane.fbhelper.fbhelperextentionbe.entity.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponse {
    private String id;
    private String device_name;
    private String device_type;

    private Boolean is_online;

    private Boolean is_active;

    private LocalDateTime create_at;

    private Integer user_id;

    public DeviceResponse(Device device) {
        this.id = device.getId();
        this.device_name = device.getDevice_name();
        this.device_type = device.getDevice_type();
        this.is_online = device.getIs_online();
        this.is_active = device.getIs_active();
        this.create_at = device.getCreated_at();
        this.user_id = device.getUser().getId();
    }
}
