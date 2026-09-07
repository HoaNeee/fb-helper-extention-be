package com.hoane.fbhelper.fbhelperextentionbe.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.PostConfigRequest;
import com.hoane.fbhelper.fbhelperextentionbe.entity.PostConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostConfigResponse {
    private Integer maxGroupPerBatch;

    private Integer timeDelayClickToPost;

    private Integer timeDelayFillContent;

    private Integer timeDelayFillFile;

    private Integer timeDelayPost;

    private Integer timeDelayOpenNewTab;

    private Long lastTimePost;

    private Boolean isShuffleGroupNeedPost;

    private Boolean isSpammed;

    public PostConfigResponse(PostConfig postConfig) {
        this.maxGroupPerBatch = postConfig.getMaxGroupPerBatch();
        this.timeDelayClickToPost = postConfig.getTimeDelayClickToPost();
        this.timeDelayFillContent = postConfig.getTimeDelayFillContent();
        this.timeDelayFillFile = postConfig.getTimeDelayFillFile();
        this.timeDelayPost = postConfig.getTimeDelayPost();
        this.timeDelayOpenNewTab = postConfig.getTimeDelayOpenNewTab();
        this.lastTimePost = postConfig.getLastTimePost();
        this.isShuffleGroupNeedPost = postConfig.getIsShuffleGroupNeedPost();
        this.isSpammed = postConfig.getIsSpammed();
    }

    public PostConfigResponse(PostConfigRequest postConfig) {
        this.maxGroupPerBatch = postConfig.getMaxGroupPerBatch();
        this.timeDelayClickToPost = postConfig.getTimeDelayClickToPost();
        this.timeDelayFillContent = postConfig.getTimeDelayFillContent();
        this.timeDelayFillFile = postConfig.getTimeDelayFillFile();
        this.timeDelayPost = postConfig.getTimeDelayPost();
        this.timeDelayOpenNewTab = postConfig.getTimeDelayOpenNewTab();
        this.lastTimePost = postConfig.getLastTimePost();
        this.isShuffleGroupNeedPost = postConfig.getIsShuffleGroupNeedPost();
        this.isSpammed = postConfig.getIsSpammed();
    }

}
