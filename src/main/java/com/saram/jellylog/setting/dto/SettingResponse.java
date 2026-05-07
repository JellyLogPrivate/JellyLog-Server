package com.saram.jellylog.setting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class SettingResponse {
    // 알림 설정 변경 응답
    @Getter
    @AllArgsConstructor
    public static class Notification {
        private boolean userNotificationEnabled;
    }

    // 소리 설정 변경 응답
    @Getter
    @AllArgsConstructor
    public static class Sound {
        private boolean userSoundEnabled;
    }

    // 이메일 설정 변경 응답
    @Getter
    @AllArgsConstructor
    public static class Email {
        private boolean userEmailEnabled;
    }
}
