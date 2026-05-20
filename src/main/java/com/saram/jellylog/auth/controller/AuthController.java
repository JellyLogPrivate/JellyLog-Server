package com.saram.jellylog.auth.controller;

import com.saram.jellylog.auth.dto.AuthUserResponse;
import com.saram.jellylog.auth.dto.AuthTokenResponse;
import com.saram.jellylog.auth.dto.RefreshTokenRequest;
import com.saram.jellylog.auth.service.AuthService;
import com.saram.jellylog.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.refresh(request.getUserRefreshToken())));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal Long userCode) {
        authService.logout(userCode);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthUserResponse>> me(@AuthenticationPrincipal Long userCode) {
        return ResponseEntity.ok(ApiResponse.success(authService.me(userCode)));
    }

}
