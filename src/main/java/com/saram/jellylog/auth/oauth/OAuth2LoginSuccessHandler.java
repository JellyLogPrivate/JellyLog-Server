package com.saram.jellylog.auth.oauth;

import com.saram.jellylog.auth.dto.AuthTokenResponse;
import com.saram.jellylog.auth.service.AuthService;
import com.saram.jellylog.global.ApiResponse;
import com.saram.jellylog.user.entity.User;
import com.saram.jellylog.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final String GOOGLE_PROVIDER_ID_ATTRIBUTE = "sub";

    private final UserRepository userRepository;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public OAuth2LoginSuccessHandler(
            UserRepository userRepository,
            AuthService authService,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String providerId = oauth2User.getAttribute(GOOGLE_PROVIDER_ID_ATTRIBUTE);
        User user = userRepository.findByUserAuthProviderId(providerId)
                .orElseThrow(() -> new IllegalStateException("OAuth2 사용자를 찾을 수 없습니다."));
        AuthTokenResponse loginResponse = authService.issueTokens(user);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), ApiResponse.success(loginResponse));
    }
}
