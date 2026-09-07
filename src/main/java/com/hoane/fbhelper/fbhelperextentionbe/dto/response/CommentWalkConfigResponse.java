package com.hoane.fbhelper.fbhelperextentionbe.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.CommentWalkConfigRequest;
import com.hoane.fbhelper.fbhelperextentionbe.entity.CommentWalkConfig;
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
public class CommentWalkConfigResponse {
    private Boolean isCommentWalk;

    private Boolean isSpammedCommentWalk;

    private Integer maxCommentWalkPerBatch;

    private Boolean isSkipPostNotInGroup;

    private Boolean isCombineStrictlyTitleGroup;

    private Integer timeDelayFillContentCommentWalkMin;

    private Integer timeDelayFillContentCommentWalkMax;

    private Integer timeDelayFillFileCommentWalk;

    private Integer timeDelaySubmitCommentWalk;

    private Integer maxRateValueContentQueryIncludesCommonCommentWalk;

    private String commentWalkArea;

    private List<String> contentQueryIncludesCommonCommentWalk;

    private List<String> contentQueryExcludesCommonCommentWalk;

    private List<String> keywordsCertainChoiceCommentWalk;

    private Long lastTimeCommentWalk;

    public CommentWalkConfigResponse(CommentWalkConfig config) {
        if (config == null) return;
        this.isCommentWalk = config.getIsCommentWalk();
        this.isSpammedCommentWalk = config.getIsSpammedCommentWalk();
        this.maxCommentWalkPerBatch = config.getMaxCommentWalkPerBatch();
        this.isSkipPostNotInGroup = config.getIsSkipPostNotInGroup();
        this.isCombineStrictlyTitleGroup = config.getIsCombineStrictlyTitleGroup();
        this.timeDelayFillContentCommentWalkMin = config.getTimeDelayFillContentCommentWalkMin();
        this.timeDelayFillContentCommentWalkMax = config.getTimeDelayFillContentCommentWalkMax();
        this.timeDelayFillFileCommentWalk = config.getTimeDelayFillFileCommentWalk();
        this.timeDelaySubmitCommentWalk = config.getTimeDelaySubmitCommentWalk();
        this.maxRateValueContentQueryIncludesCommonCommentWalk = config.getMaxRateValueContentQueryIncludesCommonCommentWalk();
        this.commentWalkArea = config.getCommentWalkArea();
        this.contentQueryIncludesCommonCommentWalk = config.getContentQueryIncludesCommonCommentWalk();
        this.contentQueryExcludesCommonCommentWalk = config.getContentQueryExcludesCommonCommentWalk();
        this.keywordsCertainChoiceCommentWalk = config.getKeywordsCertainChoiceCommentWalk();
        this.lastTimeCommentWalk = config.getLastTimeCommentWalk();
    }

    public CommentWalkConfigResponse(CommentWalkConfigRequest config) {
        this.isCommentWalk = config.getIsCommentWalk();
        this.isSpammedCommentWalk = config.getIsSpammedCommentWalk();
        this.maxCommentWalkPerBatch = config.getMaxCommentWalkPerBatch();
        this.isSkipPostNotInGroup = config.getIsSkipPostNotInGroup();
        this.isCombineStrictlyTitleGroup = config.getIsCombineStrictlyTitleGroup();
        this.timeDelayFillContentCommentWalkMin = config.getTimeDelayFillContentCommentWalkMin();
        this.timeDelayFillContentCommentWalkMax = config.getTimeDelayFillContentCommentWalkMax();
        this.timeDelayFillFileCommentWalk = config.getTimeDelayFillFileCommentWalk();
        this.timeDelaySubmitCommentWalk = config.getTimeDelaySubmitCommentWalk();
        this.maxRateValueContentQueryIncludesCommonCommentWalk = config.getMaxRateValueContentQueryIncludesCommonCommentWalk();
        this.commentWalkArea = config.getCommentWalkArea();
        this.contentQueryIncludesCommonCommentWalk = config.getContentQueryIncludesCommonCommentWalk();
        this.contentQueryExcludesCommonCommentWalk = config.getContentQueryExcludesCommonCommentWalk();
        this.keywordsCertainChoiceCommentWalk = config.getKeywordsCertainChoiceCommentWalk();
        this.lastTimeCommentWalk = config.getLastTimeCommentWalk();
    }
}
