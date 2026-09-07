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
public class CommentWalkConfigRequest {

    @NotBlank(message = "Device id can not null or empty")
    @Size(min = 8, message = "Device id must be at least 8 characters")
    private String deviceId;

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

}
