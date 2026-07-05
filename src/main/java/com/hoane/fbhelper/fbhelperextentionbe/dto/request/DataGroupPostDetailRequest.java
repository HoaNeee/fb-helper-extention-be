package com.hoane.fbhelper.fbhelperextentionbe.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGroupPostDetailRequest {

    @NotNull(message = "data_group_post_id can not null")
    private int data_group_post_id;

    @NotNull(message = "device_id can not null")
    private String device_id;

    @NotNull(message = "is_active can not null")
    private boolean is_active;
}
