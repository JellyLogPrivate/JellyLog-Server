package com.saram.jellylog.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenResponse {
    private String userRefreshToken;
    private String userAccessToken;
    private Long userCode;
}
