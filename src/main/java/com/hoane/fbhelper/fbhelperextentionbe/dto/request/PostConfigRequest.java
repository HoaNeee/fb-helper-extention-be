package com.hoane.fbhelper.fbhelperextentionbe.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostConfigRequest {
    @NotBlank(message = "Device id can not null or empty")
    @Size(min = 8, message = "Device id must be at least 8 characters")
    private String deviceId;

    private Integer maxGroupPerBatch;

    private Integer timeDelayClickToPost;

    private Integer timeDelayFillContent;

    private Integer timeDelayFillFile;

    private Integer timeDelayPost;

    private Integer timeDelayOpenNewTab;
    
    private Boolean isShuffleGroupNeedPost;

    private Boolean isSpammed;

    private Long lastTimePost;
}
