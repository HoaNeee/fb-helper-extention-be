package com.hoane.fbhelper.fbhelperextentionbe.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceSettingRequest {

    @NotBlank(message = "Device id can not null or empty")
    @Size(min = 8, message = "Device id must be at least 8 characters")
    private String deviceId;

    private Boolean isFixStealFocus;
    private Boolean isFixStealAllFocus;
    private Boolean isShuffleGroupNeedPost;
    private Boolean isRandomBreakBatch;
    private Boolean isRandomTimePost;
    private Boolean isScheduler;
    private Boolean isSpecialFrameHours;

    private Boolean isSpammed;
    private Boolean isCommentWhenPost;
    private Boolean isInteractBatch;

    private Integer timeDelayClickToPost;
    private Integer timeDelayFillContent;
    private Integer timeDelayFillFile;
    private Integer timeDelayPost;
    private Integer timeDelayOpenNewTab;

    private Integer maxGroupPerBatch;
    private List<String> strictlyTitleMatchGroups;

    private Long lastTimePost;

}
