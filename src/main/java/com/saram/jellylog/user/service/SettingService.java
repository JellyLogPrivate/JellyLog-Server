package com.saram.jellylog.user.service;

import com.saram.jellylog.global.exception.NotFoundException;
import com.saram.jellylog.user.dto.SettingRequest;
import com.saram.jellylog.user.dto.SettingResponse;
import com.saram.jellylog.user.entity.User;
import com.saram.jellylog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettingService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public SettingResponse getSettings(Long userCode) {
        User user = getUser(userCode);
        return new SettingResponse(
                user.getUserNotificationEnabled(),
                user.getUserSoundEnabled(),
                user.getUserEmailEnabled()
        );
    }

    public SettingResponse updateSettings(Long userCode, SettingRequest request) {
        User user = getUser(userCode);
        user.setUserNotificationEnabled(request.getUserNotificationEnabled());
        user.setUserSoundEnabled(request.getUserSoundEnabled());
        user.setUserEmailEnabled(request.getUserEmailEnabled());
        return new SettingResponse(
                user.getUserNotificationEnabled(),
                user.getUserSoundEnabled(),
                user.getUserEmailEnabled()
        );
    }

    private User getUser(Long userCode) {
        return userRepository.findById(userCode)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    }
}
