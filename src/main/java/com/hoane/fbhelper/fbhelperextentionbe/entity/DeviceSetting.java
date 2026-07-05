package com.hoane.fbhelper.fbhelperextentionbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "device_settings")
@Builder
@Data
@AllArgsConstructor
public class DeviceSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private boolean is_fix_steal_focus = true;
    private boolean is_shuffle_groups_need_post = true;
    private boolean is_random_break_batch = false;
    private boolean is_random_time_post = false;
    private boolean is_scheduler = false;

    private boolean is_spammed = false;
    private boolean is_comment_when_post = false;
    private boolean is_interact_batch = false;

    private Integer time_delay_click_to_post;
    private Integer time_delay_fill_content;
    private Integer time_delay_fill_file;
    private Integer time_delay_post;
    private Integer time_delay_open_new_tab;

    private Long last_time_post;

    @OneToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    public DeviceSetting() {
    }
}
