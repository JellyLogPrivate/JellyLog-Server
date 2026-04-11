package com.saram.jellylog.login.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginRequest {
    private String userAuthProvier;
    private String userAuthProviderId;
}
