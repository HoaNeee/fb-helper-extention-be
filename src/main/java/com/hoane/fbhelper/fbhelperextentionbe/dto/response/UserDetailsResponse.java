package com.hoane.fbhelper.fbhelperextentionbe.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class UserDetailsResponse {
    private String id;
    private String username;
    private String email;
    private String name;
    private UserStatus status;
    private String avatar;
    private Boolean isPremium;
    private LocalDateTime createdAt;
    private Role role;

    public UserDetailsResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.status = user.getStatus();
        this.avatar = user.getAvatar();
        this.isPremium = user.isMember();
        this.createdAt = user.getCreatedAt();
    }

    public UserDetailsResponse(User user, Role role) {
        this(user);
        this.role = role;
    }
}