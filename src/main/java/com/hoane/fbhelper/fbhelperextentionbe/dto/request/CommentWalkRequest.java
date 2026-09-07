package com.hoane.fbhelper.fbhelperextentionbe.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnCreate;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.action.OnUpdate;
import com.hoane.fbhelper.fbhelperextentionbe.utils.converter.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

public class CommentWalkRequest {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class BaseRequest {
        @NotEmpty(message = "Title query search can not null or empty", groups = {OnCreate.class})
        @Size(min = 1, message = "Title query search must have at least 1 element", groups = {OnCreate.class, OnUpdate.class})
        private List<String> titleQuerySearchs;

        private String name;

        private List<String> files;

        private List<String> contents;

        private List<String> keywordQueryIncludes;

        private List<String> keywordQueryExcludes;

        private List<String> keywordsCertainChoice;

        private Integer matchRateValueContentQueryIncludes;

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
    public static class CommentWalkDetailRequest {
        @NotNull(message = "comment_walk_id can not null")
        private String commentWalkId;

        @NotBlank(message = "device_id can not null")
        @Size(min = 8, message = "device_id must be at least 8 characters")
        private String deviceId;

        @NotNull(message = "is_active can not null")
        private Boolean isActive;
    }
}
