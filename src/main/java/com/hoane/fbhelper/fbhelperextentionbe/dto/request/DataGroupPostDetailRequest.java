package com.hoane.fbhelper.fbhelperextentionbe.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGroupPostDetailRequest {

    @NotNull(message = "data_group_post_id can not null")
    private Integer data_group_post_id;

    @NotBlank(message = "device_id can not null")
    @Size(min = 8, max = 8, message = "device_id must be exactly 8 characters")
    private String device_id;

    @NotNull(message = "is_active can not null")
    private Boolean is_active;
}
