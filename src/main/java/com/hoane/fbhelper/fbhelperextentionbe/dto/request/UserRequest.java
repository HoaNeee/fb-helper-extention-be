package com.hoane.fbhelper.fbhelperextentionbe.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;


public class UserRequest {


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CreateUserRequest {

        @Size(min = 1, message = "Username must be greater than or equal to 1 character")
        private String username;

        @Size(min = 1, message = "Email must be greater than or equal to 1 character")
        private String email;

        @NotBlank(message = "Password can not blank")
        @NotNull(message = "Password can not null")
        private String password;

        @NotBlank(message = "Role can not blank")
        @NotNull(message = "Role can not null")
        private String role;

        @NotBlank(message = "Status can not blank")
        @NotNull(message = "Status can not null")
        private String status;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserUpdateRequest {

        @Size(min = 1, message = "Username must be greater than or equal to 1 character")
        private String username;

        @Size(min = 1, message = "Email must be greater than or equal to 1 character")
        private String email;

        private String name;

        private String avatar;
    }


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserUpdateRoleRequest {
        private String userId;

        @NotBlank(message = "Role can not blank")
        @NotNull(message = "Role can not null")
        private String role;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserUpdateStatusRequest {
        @NotBlank(message = "Status can not blank")
        @NotNull(message = "Status can not null")
        private String userId;

        @NotBlank(message = "Status can not blank")
        @NotNull(message = "Status can not null")
        private String status;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserChangePasswordRequest {
        private String oldPassword;

        @NotBlank(message = "New password can not blank")
        @NotNull(message = "New password can not null")
        private String newPassword;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AdminUpdateUserRequest {
        @Size(min = 1, message = "Username must be greater than or equal to 1 character")
        private String username;

        @Size(min = 1, message = "Email must be greater than or equal to 1 character")
        private String email;

        @Size(min = 1, message = "Role must be greater than or equal to 1 character")
        private String role;

        @Size(min = 1, message = "Status must be greater than or equal to 1 character")
        private String status;
    }
}
