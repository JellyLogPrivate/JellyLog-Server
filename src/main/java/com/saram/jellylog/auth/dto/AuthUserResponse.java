package com.saram.jellylog.auth.dto;

import com.saram.jellylog.user.entity.AuthProvider;
import com.saram.jellylog.user.entity.User;

public record AuthUserResponse(
        Long userCode,
        String userName,
        Integer userPoints,
        AuthProvider userAuthProvider
) {
    public static AuthUserResponse from(User user) {
        return new AuthUserResponse(
                user.getUserCode(),
                user.getUserName(),
                user.getUserPoints(),
                user.getUserAuthProvider()
        );
    }
}
