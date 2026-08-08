package com.hoane.fbhelper.fbhelperextentionbe.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

public class DataGroupPostRequest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DataGroupRequest {
        @NotBlank(message = "Title match can not null or empty", groups = OnCreate.class)
        @Size(min = 1, groups = OnUpdate.class, message = "Title match must be greater than or equal to 1 character")
        private String title;

        private String name;

        private Integer priority;

        private List<String> files;

        private List<String> contents;

        @Min(value = 0, message = "From member must be greater than or equal to 0")
        private Integer fromMember;

        @Min(value = 0, message = "To member must be greater than or equal to 0")
        private Integer toMember;

        @Size(min = 8, message = "device_id must be at least 8 characters")
        private String deviceId;

        private String id;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DataGroupPostDetailRequest {
        @NotNull(message = "data_group_post_id can not null")
        private String dataGroupPostId;

        @NotBlank(message = "device_id can not null")
        @Size(min = 8, message = "device_id must be at least 8 characters")
        private String deviceId;

        @NotNull(message = "is_active can not null")
        private Boolean isActive;
    }


}

