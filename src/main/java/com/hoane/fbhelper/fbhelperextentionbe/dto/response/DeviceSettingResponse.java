package com.hoane.fbhelper.fbhelperextentionbe.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.entity.*;
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
    private Boolean isRandomTimePost;
    private Boolean isScheduler;
    private Boolean isSpecialFrameHours;
    private Boolean isCommentWhenPost;
    private Boolean isInteractBatch;

    private Integer timeBreakWhenSpammed;

    private List<String> strictlyTitleMatchGroups;

    private PriorityTask priorityTask;

    private PostConfigResponse postConfig;

    private CommentWalkConfigResponse commentWalkConfig;

    public DeviceSettingResponse(DeviceSetting deviceSetting, PostConfig postConfig, CommentWalkConfig commentWalkConfig) {
        this.isCommentWhenPost = deviceSetting.getIsCommentWhenPost();
        this.isFixStealFocus = deviceSetting.getIsFixStealFocus();
        this.isInteractBatch = deviceSetting.getIsInteractBatch();
        this.isScheduler = deviceSetting.getIsScheduler();
        this.strictlyTitleMatchGroups = deviceSetting.getStrictlyMatchTitleGroups();
        this.timeBreakWhenSpammed = deviceSetting.getTimeBreakWhenSpammed();

        this.isRandomBreakBatch = deviceSetting.getIsRandomBreakBatch();
        this.isRandomTimePost = deviceSetting.getIsRandomTimePost();
        this.isFixStealAllFocus = deviceSetting.getIsFixStealAllFocus();
        this.isSpecialFrameHours = deviceSetting.getIsSpecialFrameHours();

        this.priorityTask = new PriorityTask(deviceSetting.getPriorityTaskPost(), deviceSetting.getPriorityTaskCommentWalk());

        this.postConfig = new PostConfigResponse(postConfig);

        this.commentWalkConfig = new CommentWalkConfigResponse(commentWalkConfig);
    }


    public DeviceSettingResponse(DeviceSetting deviceSetting) {
        this.isCommentWhenPost = deviceSetting.getIsCommentWhenPost();
        this.isFixStealFocus = deviceSetting.getIsFixStealFocus();
        this.isInteractBatch = deviceSetting.getIsInteractBatch();

        this.isFixStealAllFocus = deviceSetting.getIsFixStealAllFocus();
        this.isRandomBreakBatch = deviceSetting.getIsRandomBreakBatch();
        this.isRandomTimePost = deviceSetting.getIsRandomTimePost();
        this.isScheduler = deviceSetting.getIsScheduler();
        this.isSpecialFrameHours = deviceSetting.getIsSpecialFrameHours();
        this.timeBreakWhenSpammed = deviceSetting.getTimeBreakWhenSpammed();
        this.strictlyTitleMatchGroups = deviceSetting.getStrictlyMatchTitleGroups();

        this.priorityTask = new PriorityTask(deviceSetting.getPriorityTaskPost(), deviceSetting.getPriorityTaskCommentWalk());
    }


}
