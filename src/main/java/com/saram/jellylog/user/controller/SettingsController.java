package com.saram.jellylog.user.controller;

import com.saram.jellylog.global.ApiResponse;
import com.saram.jellylog.user.dto.SettingRequest;
import com.saram.jellylog.user.dto.SettingResponse;
import com.saram.jellylog.user.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingService settingService;

    @GetMapping("/settings")
    public ResponseEntity<ApiResponse<SettingResponse>> getSettings(@AuthenticationPrincipal Long userCode) {
        return ResponseEntity.ok(ApiResponse.success(settingService.getSettings(userCode)));
    }

    @PutMapping("/settings")
    public ResponseEntity<ApiResponse<SettingResponse>> updateSettings(
            @AuthenticationPrincipal Long userCode,
            @RequestBody SettingRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(settingService.updateSettings(userCode, request)));
    }
}
