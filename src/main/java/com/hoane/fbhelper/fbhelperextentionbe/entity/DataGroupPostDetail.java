package com.hoane.fbhelper.fbhelperextentionbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "data_group_post_details")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGroupPostDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private boolean is_active;

    @ManyToOne
    @JoinColumn(name = "data_group_post_id", nullable = false, updatable = false)
    private DataGroupPost data_group_post;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false, updatable = false)
    private Device device;
}
