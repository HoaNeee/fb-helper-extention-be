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
    @Column(columnDefinition = "VARCHAR(10)", unique = true, nullable = false, updatable = false)
    private String id;

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

    @Builder.Default
    private Boolean isDeleted = false;

    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'PENDING'")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING;

    @Builder.Default
    private Boolean isNewUser = false;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();


    public boolean isMember() {
        return this.isActive() && (this.isRole(Role.ROLE_MEMBER) || this.isRole(Role.ROLE_ADMIN));
    }

    public boolean isStatus(UserStatus status) {
        return this.status.equals(status);
    }

    public boolean isRole(Role role) {
        return this.role.equals(role);
    }

    public boolean isActive() {
        return this.isStatus(UserStatus.ACTIVE);
    }


    //Switch using role = member
//    @Builder.Default
//    private Boolean is_premium = false;
}
