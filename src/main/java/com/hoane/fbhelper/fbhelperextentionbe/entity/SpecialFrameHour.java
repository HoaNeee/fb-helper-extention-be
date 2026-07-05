package com.hoane.fbhelper.fbhelperextentionbe.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "special_frame_hours")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialFrameHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer start_time;
    private Integer end_time;

    private Integer max_group;

    private String apply_date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
