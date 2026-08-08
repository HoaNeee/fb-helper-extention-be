package com.hoane.fbhelper.fbhelperextentionbe.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.SchedulerType;
import com.hoane.fbhelper.fbhelperextentionbe.utils.converter.ListConverter;
import com.hoane.fbhelper.fbhelperextentionbe.utils.converter.SchedulerTimeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;


@Entity
@Table(name = "scheduler_details")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SchedulerDetail {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Builder.Default
    private SchedulerType schedulerType = SchedulerType.DAILY_HOURS;

    private Integer schedulerTimeValue;

    @Convert(converter = SchedulerTimeConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<SchedulerTime> schedulerTimeList;

    @ManyToOne
    @JoinColumn(name = "scheduler_id", nullable = false)
    private Scheduler scheduler;
}
