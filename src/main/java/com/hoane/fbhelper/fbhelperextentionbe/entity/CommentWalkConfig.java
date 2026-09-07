package com.hoane.fbhelper.fbhelperextentionbe.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.utils.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Entity
@Table(name = "comment_walk_configs")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentWalkConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Boolean isCommentWalk = false;

    private Boolean isSpammedCommentWalk = false;

    private Integer maxCommentWalkPerBatch = Constant.MAX_COMMENT_WALK_PER_BATCH;

    private Boolean isSkipPostNotInGroup = false;

    private Boolean isCombineStrictlyTitleGroup = false;

    private Integer timeDelayFillContentCommentWalkMin = Constant.TIME_DELAY_FILL_CONTENT_COMMENT_WALK_MIN;

    private Integer timeDelayFillContentCommentWalkMax = Constant.TIME_DELAY_FILL_CONTENT_COMMENT_WALK_MAX;

    private Integer timeDelayFillFileCommentWalk = Constant.TIME_DELAY_FILL_FILE_COMMENT_WALK;

    private Integer timeDelaySubmitCommentWalk = Constant.TIME_DELAY_SUBMIT_COMMENT_WALK;

    private Integer maxRateValueContentQueryIncludesCommonCommentWalk = Constant.MAX_RATE_VALUE_CONTENT_QUERY_INCLUDES_COMMON_COMMENT_WALK;

    private String commentWalkArea = Constant.COMMENT_WALK_AREA;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> contentQueryIncludesCommonCommentWalk = Constant.CONTENT_QUERY_INCLUDES_COMMON_COMMENT_WALK;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> contentQueryExcludesCommonCommentWalk = Constant.CONTENT_QUERY_EXCLUDES_COMMON_COMMENT_WALK;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> keywordsCertainChoiceCommentWalk = Constant.KEYWORDS_CERTAIN_CHOICE_COMMENT_WALK;

    private Long lastTimeCommentWalk;


    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false, updatable = false)
    private Device device;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;
}
