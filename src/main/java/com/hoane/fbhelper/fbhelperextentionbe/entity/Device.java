package com.hoane.fbhelper.fbhelperextentionbe.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Device {
    @Id
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(8)", updatable = false)
    private String id;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String device_name;

    @Column(columnDefinition = "VARCHAR(100)")
    private String device_type;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean is_active;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean is_online;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
