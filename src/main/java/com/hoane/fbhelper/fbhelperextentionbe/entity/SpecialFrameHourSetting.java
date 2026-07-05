package com.hoane.fbhelper.fbhelperextentionbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "special_frame_hour_settings")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialFrameHourSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private boolean is_active;

    @ManyToOne
    @JoinColumn(name = "special_frame_hour_id", nullable = false)
    private SpecialFrameHour specialFrameHour;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
}
