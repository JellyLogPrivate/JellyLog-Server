package com.saram.jellylog.auth.oauth;

import com.saram.jellylog.user.entity.AuthProvider;
import com.saram.jellylog.user.entity.User;
import com.saram.jellylog.user.repository.UserRepository;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final String GOOGLE_REGISTRATION_ID = "google";
    private static final String GOOGLE_PROVIDER_ID_ATTRIBUTE = "sub";
    private static final String GOOGLE_NAME_ATTRIBUTE = "name";

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (!GOOGLE_REGISTRATION_ID.equals(registrationId)) {
            throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + registrationId);
        }

        Map<String, Object> attributes = oauth2User.getAttributes();
        String providerId = getRequiredAttribute(attributes, GOOGLE_PROVIDER_ID_ATTRIBUTE);
        String userName = getAttributeOrDefault(attributes, GOOGLE_NAME_ATTRIBUTE, "사용자님");

        User user = userRepository.findByUserAuthProviderId(providerId)
                .map(existingUser -> updateUserName(existingUser, userName))
                .orElseGet(() -> createGoogleUser(providerId, userName));

        userRepository.save(user);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                GOOGLE_PROVIDER_ID_ATTRIBUTE
        );
    }

    private User createGoogleUser(String providerId, String userName) {
        User user = new User();
        user.setUserAuthProvider(AuthProvider.GOOGLE);
        user.setUserAuthProviderId(providerId);
        user.setUserName(userName);
        return user;
    }

    private User updateUserName(User user, String userName) {
        user.setUserName(userName);
        return user;
    }

    private String getRequiredAttribute(Map<String, Object> attributes, String attributeName) {
        Object value = attributes.get(attributeName);

        if (value == null || value.toString().isBlank()) {
            throw new OAuth2AuthenticationException("Missing required OAuth2 attribute: " + attributeName);
        }

        return value.toString();
    }

    private String getAttributeOrDefault(Map<String, Object> attributes, String attributeName, String defaultValue) {
        Object value = attributes.get(attributeName);

        if (value == null || value.toString().isBlank()) {
            return defaultValue;
        }

        return value.toString();
    }
}
