package com.hoane.fbhelper.fbhelperextentionbe.entity;


import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.SchedulerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "schedulers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Scheduler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private SchedulerType schedulerType = SchedulerType.DAILY_HOURS;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false, updatable = false)
    private Device device;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;
}
