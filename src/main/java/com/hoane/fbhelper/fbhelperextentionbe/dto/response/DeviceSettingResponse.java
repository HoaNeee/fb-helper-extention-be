package com.hoane.fbhelper.fbhelperextentionbe.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.entity.*;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.DeviceSettingStatus;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DeviceSettingResponse {
    //    private String deviceId;
    private Boolean isFixStealFocus;
    private Boolean isFixStealAllFocus;
    private Boolean isRandomBreakBatch;
    private Boolean isRandomTimeTask;
    private Boolean isScheduler;
    private Boolean isSpecialFrameHours;
    private Boolean isCommentWhenPost;
    private Boolean isInteractBatch;
    private Boolean isStopTask;

    private Boolean isExecutePriorityTask;

    private Integer timeBreakWhenSpammed;

    private List<String> strictlyTitleMatchGroups;

    private PriorityTask priorityTask;

    private PostConfigResponse postConfig;

    private CommentWalkConfigResponse commentWalkConfig;

    private Boolean isRemoteControl;

    private DeviceSettingStatus status;

    public DeviceSettingResponse(DeviceSetting deviceSetting) {
        this.isCommentWhenPost = deviceSetting.getIsCommentWhenPost();
        this.isFixStealFocus = deviceSetting.getIsFixStealFocus();
        this.isInteractBatch = deviceSetting.getIsInteractBatch();

        this.isFixStealAllFocus = deviceSetting.getIsFixStealAllFocus();
        this.isRandomBreakBatch = deviceSetting.getIsRandomBreakBatch();
        this.isRandomTimeTask = deviceSetting.getIsRandomTimeTask();
        this.isScheduler = deviceSetting.getIsScheduler();
        this.isSpecialFrameHours = deviceSetting.getIsSpecialFrameHours();
        this.timeBreakWhenSpammed = deviceSetting.getTimeBreakWhenSpammed();
        this.strictlyTitleMatchGroups = deviceSetting.getStrictlyMatchTitleGroups();
        this.isStopTask = deviceSetting.getIsStopTask();
        this.isExecutePriorityTask = deviceSetting.getIsExecutePriorityTask();
        this.isRemoteControl = deviceSetting.getIsRemoteControl();
        this.status = deviceSetting.getStatus();

        this.priorityTask = new PriorityTask(deviceSetting.getPriorityTaskPost(), deviceSetting.getPriorityTaskCommentWalk());
    }

    public DeviceSettingResponse(DeviceSetting deviceSetting, PostConfig postConfig, CommentWalkConfig commentWalkConfig) {
        this(deviceSetting);

        if (postConfig != null) {
            this.postConfig = new PostConfigResponse(postConfig);
        }

        if (commentWalkConfig != null) {
            this.commentWalkConfig = new CommentWalkConfigResponse(commentWalkConfig);
        }

    }


}
