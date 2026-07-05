package com.hoane.fbhelper.fbhelperextentionbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "scheduler_details")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100)
    private String scheduler_type;

    private Integer scheduler_time_value;

    @Column(columnDefinition = "TEXT")
    private String scheduler_time_list;

    @ManyToOne
    @JoinColumn(name = "scheduler_id", nullable = false)
    private Scheduler scheduler;
}
