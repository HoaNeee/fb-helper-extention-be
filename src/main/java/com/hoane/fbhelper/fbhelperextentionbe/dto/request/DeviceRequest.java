package com.hoane.fbhelper.fbhelperextentionbe.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DeviceRequest {

    @NotBlank(message = "Device id can not null or empty", groups = OnCreate.class)
    private String id;

    @Size(min = 1, message = "Device name can not null or empty", groups = OnUpdate.class)
    private String device_name;

    @Size(min = 1, message = "Device type can not null or empty", groups = OnUpdate.class)
    private String device_type;

    private Boolean is_online;

    private Boolean is_active;

}
