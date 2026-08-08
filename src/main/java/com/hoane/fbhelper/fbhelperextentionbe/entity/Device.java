package com.hoane.fbhelper.fbhelperextentionbe.entity;

import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.DeviceStatus;
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
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(10)", updatable = false)
    private String id;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String deviceName;

    @Column(columnDefinition = "VARCHAR(100)")
    private String deviceType;

    @Enumerated(EnumType.STRING)
    private DeviceStatus status = DeviceStatus.ACTIVE;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
