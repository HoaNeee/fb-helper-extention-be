package com.hoane.fbhelper.fbhelperextentionbe.entity;

import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert //exclude null value
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
    private String username;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String password;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(columnDefinition = "VARCHAR(100)", unique = true)
    private String email;

    @Column(columnDefinition = "VARCHAR(100)")
    private String provider;

    @Column(columnDefinition = "VARCHAR(100)")
    private String provider_id;

    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'ROLE_USER'", nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @Column(columnDefinition = "VARCHAR(255)")
    private String avatar;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private boolean is_deleted = false;

    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'PENDING'")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime created_at = LocalDateTime.now();


}
