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
    private Integer id;

    private Boolean isFixStealFocus = true;

    private Boolean isFixStealAllFocus = false;

    private Boolean isRandomBreakBatch = false;

    private Boolean isRandomTimePost = false;

    private Boolean isScheduler = false;

    @Builder.Default
    private Boolean isSpecialFrameHours = false;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> strictlyMatchTitleGroups = Constant.STRICTLY_TITLE_MATCH_GROUPS;

    private Boolean isCommentWhenPost = false;

    private Boolean isInteractBatch = false;

    private Boolean isStopTask = false;

    private Integer timeBreakWhenSpammed = Constant.TIME_BREAK_WHEN_SPAMMED;


    private Integer priorityTaskPost = Constant.PRIORITY_TASK_POST;

    private Integer priorityTaskCommentWalk = Constant.PRIORITY_TASK_COMMENT_WALK;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false, updatable = false)
    private Device device;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;
}
