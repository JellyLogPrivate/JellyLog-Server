package com.saram.jellylog.auth.controller;

import com.saram.jellylog.auth.dto.AuthUserResponse;
import com.saram.jellylog.auth.dto.LoginRequest;
import com.saram.jellylog.auth.dto.LoginResponse;
import com.saram.jellylog.auth.dto.RefreshTokenRequest;
import com.saram.jellylog.auth.service.AuthService;
import com.saram.jellylog.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.refresh(request.getUserRefreshToken())));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(Authentication authentication) {
        authService.logout(currentUserCode(authentication));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthUserResponse>> me(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success(authService.me(currentUserCode(authentication))));
    }

    private Long currentUserCode(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }
}
