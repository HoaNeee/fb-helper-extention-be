package com.hoane.fbhelper.fbhelperextentionbe.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginResponse {
    private String token;
    private String userId;
    private String username;
    private UserStatus status;

    private Role role;

    public LoginResponse(String token, String userId, String username, UserStatus status) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.status = status;
    }

    public LoginResponse(String token, String userId, String username, UserStatus status, Role role) {
        this(token, userId, username, status);
        this.role = role;
    }
}
