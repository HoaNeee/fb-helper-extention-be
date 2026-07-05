package com.hoane.fbhelper.fbhelperextentionbe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @Id
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(8)")
    private String id;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String device_name;

    @Column(columnDefinition = "VARCHAR(100)")
    private String device_type;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean is_active;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean is_online;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
