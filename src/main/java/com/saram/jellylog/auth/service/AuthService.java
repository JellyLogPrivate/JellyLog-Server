package com.saram.jellylog.auth.service;

import com.saram.jellylog.auth.dto.AuthUserResponse;
import com.saram.jellylog.auth.dto.AuthTokenResponse;
import com.saram.jellylog.auth.security.JwtTokenProvider;
import com.saram.jellylog.user.entity.User;
import com.saram.jellylog.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public AuthTokenResponse refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("refresh token은 필수입니다.");
        }

        jwtTokenProvider.validateRefreshToken(refreshToken);
        User user = userRepository.findByUserRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("저장된 refresh token을 찾을 수 없습니다."));

        if (user.getUserRefreshTokenExpiredAt() == null
                || LocalDateTime.now().isAfter(user.getUserRefreshTokenExpiredAt())) {
            clearRefreshToken(user);
            throw new IllegalArgumentException("refresh token이 만료되었습니다.");
        }

        return issueTokens(user);
    }

    @Transactional
    public void logout(Long userCode) {
        User user = getUser(userCode);
        clearRefreshToken(user);
    }

    @Transactional(readOnly = true)
    public AuthUserResponse me(Long userCode) {
        return AuthUserResponse.from(getUser(userCode));
    }

    @Transactional
    public AuthTokenResponse issueTokens(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user.getUserCode());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserCode());

        user.setUserRefreshToken(refreshToken);
        user.setUserRefreshTokenExpiredAt(LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshTokenValiditySeconds()));
        userRepository.save(user);

        return new AuthTokenResponse(refreshToken, accessToken, user.getUserCode());
    }

    private User getUser(Long userCode) {
        return userRepository.findById(userCode)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private void clearRefreshToken(User user) {
        user.setUserRefreshToken(null);
        user.setUserRefreshTokenExpiredAt(null);
    }

}
