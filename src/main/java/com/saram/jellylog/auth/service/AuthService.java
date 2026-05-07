package com.saram.jellylog.auth.service;

import com.saram.jellylog.auth.dto.AuthUserResponse;
import com.saram.jellylog.auth.dto.LoginRequest;
import com.saram.jellylog.auth.dto.LoginResponse;
import com.saram.jellylog.auth.security.JwtTokenProvider;
import com.saram.jellylog.user.entity.AuthProvider;
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
    public LoginResponse login(LoginRequest request) {
        AuthProvider authProvider = parseAuthProvider(request.getResolvedAuthProvider());
        if (request.getUserAuthProviderId() == null || request.getUserAuthProviderId().isBlank()) {
            throw new IllegalArgumentException("userAuthProviderId는 필수입니다.");
        }

        User user = userRepository.findByUserAuthProviderId(request.getUserAuthProviderId())
                .orElseGet(() -> createUser(authProvider, request.getUserAuthProviderId()));

        if (user.getUserAuthProvider() != authProvider) {
            throw new IllegalArgumentException("가입된 인증 제공자가 일치하지 않습니다.");
        }

        return issueTokens(user);
    }

    @Transactional
    public LoginResponse refresh(String refreshToken) {
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
    public LoginResponse issueTokens(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user.getUserCode());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserCode());

        user.setUserRefreshToken(refreshToken);
        user.setUserRefreshTokenExpiredAt(LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshTokenValiditySeconds()));

        return new LoginResponse(refreshToken, accessToken, user.getUserCode());
    }

    private User createUser(AuthProvider authProvider, String providerId) {
        User user = new User();
        user.setUserAuthProvider(authProvider);
        user.setUserAuthProviderId(providerId);
        user.setUserName("사용자님");
        return userRepository.save(user);
    }

    private User getUser(Long userCode) {
        return userRepository.findById(userCode)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private void clearRefreshToken(User user) {
        user.setUserRefreshToken(null);
        user.setUserRefreshTokenExpiredAt(null);
    }

    private AuthProvider parseAuthProvider(String authProvider) {
        if (authProvider == null || authProvider.isBlank()) {
            return AuthProvider.GOOGLE;
        }

        try {
            return AuthProvider.valueOf(authProvider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 인증 제공자입니다: " + authProvider);
        }
    }
}
