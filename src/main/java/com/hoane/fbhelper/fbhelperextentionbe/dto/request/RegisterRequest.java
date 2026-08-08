package com.hoane.fbhelper.fbhelperextentionbe.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username can not null or empty")
    private String username;

    @NotBlank(message = "Email can not null or empty")
    private String email;

    @NotBlank(message = "Password can not null or empty")
    private String password;
}
