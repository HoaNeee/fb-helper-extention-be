package com.hoane.fbhelper.fbhelperextentionbe.dto.request;

import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.LoginLocation;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "user can not empty")
    private String username;

    @NotBlank(message = "password can not empty")
    private String password;

    private LoginLocation loginLocation = LoginLocation.DEVICE; //DEVICE, WEB
}
