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
@Table(name = "device_settings")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DeviceSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Boolean isFixStealFocus = true;
    private Boolean isFixStealAllFocus = false;
    private Boolean isShuffleGroupNeedPost = true;
    private Boolean isRandomBreakBatch = false;
    private Boolean isRandomTimePost = false;
    private Boolean isScheduler = false;
    private Boolean isSpecialFrameHours = false;

    private Boolean isSpammed = false;
    private Boolean isCommentWhenPost = false;
    private Boolean isInteractBatch = false;

    private Integer timeDelayClickToPost = Constant.TIME_DELAY_TO_POST;
    private Integer timeDelayFillContent = Constant.TIME_DELAY_TO_POST;
    private Integer timeDelayFillFile = Constant.TIME_DELAY_TO_POST;
    private Integer timeDelayPost = Constant.TIME_DELAY_TO_POST;
    private Integer timeDelayOpenNewTab = Constant.TIME_DELAY_TO_POST;

    private Integer maxGroupPerBatch = Constant.MAX_GROUP_PER_BATCH;

    private Long lastTimePost;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> strictlyTitleMatchGroups = Constant.STRICTLY_TITLE_MATCH_GROUPS;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false, updatable = false)
    private Device device;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;
}
