package com.saram.jellylog.login.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginRequest {
    private String userAuthProvider;
    private String userAuthProvdier;
    private String userAuthProviderId;

    public String getResolvedAuthProvider() {
        if (userAuthProvider != null && !userAuthProvider.isBlank()) {
            return userAuthProvider;
        }

        return userAuthProvdier;
    }
}
