package com.hoane.fbhelper.fbhelperextentionbe.controller;

import com.hoane.fbhelper.fbhelperextentionbe.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.LoginRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RegisterRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RequestModels;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.LoginResponse;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import com.hoane.fbhelper.fbhelperextentionbe.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(HttpServletRequest rawRequest, @RequestBody LoginRequest loginRequest) {
        String ipAddress = RequestUtils.getClientIp(rawRequest);
        LoginResponse loginResponse = authService.login(loginRequest, ipAddress);
        return ApiResponse.success(200, "Login successful", loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequest registerRequest) {
        return ApiResponse.success(201, "User registered successfully", null);
    }

    @PostMapping("/verify-token")
    public ResponseEntity<ApiResponse<Boolean>> verifyToken(HttpServletRequest rawRequest, @RequestBody RequestModels.VerifyTokenRequest verifyTokenRequest) {
        String ipAddress = RequestUtils.getClientIp(rawRequest);

        Boolean isTokenValid = authService.verifyToken(verifyTokenRequest, ipAddress);

        return ApiResponse.success(201, "Verify token successfully", isTokenValid);
    }


}
