package com.saram.jellylog.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SettingRequest {
    private Boolean userNotificationEnabled;
    private Boolean userSoundEnabled;
    private Boolean userEmailEnabled;
}
