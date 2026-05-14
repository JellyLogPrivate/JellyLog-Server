package com.saram.jellylog.user.controller;

import com.saram.jellylog.global.ApiResponse;
import com.saram.jellylog.user.dto.ProfileRequest;
import com.saram.jellylog.user.dto.ProfileResponse;
import com.saram.jellylog.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/user")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(@AuthenticationPrincipal Long userCode) {
        return ResponseEntity.ok(ApiResponse.success(profileService.getProfile(userCode)));
    }

    @PutMapping("/profile")
    public  ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @AuthenticationPrincipal Long userCode,
            @RequestBody ProfileRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(profileService.updateProfile(userCode, request)));
    }
}
