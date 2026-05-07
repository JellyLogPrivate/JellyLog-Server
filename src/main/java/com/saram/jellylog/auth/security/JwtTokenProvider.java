package com.saram.jellylog.auth.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Component
public class JwtTokenProvider {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder BASE64_URL_DECODER = Base64.getUrlDecoder();

    private final ObjectMapper objectMapper;
    private final byte[] secret;
    private final long accessTokenValiditySeconds;
    private final long refreshTokenValiditySeconds;

    public JwtTokenProvider(
            ObjectMapper objectMapper,
            @Value("${jwt.secret:${spring.application.name}-local-development-secret-change-me}") String secret,
            @Value("${jwt.access-token-validity-seconds:3600}") long accessTokenValiditySeconds,
            @Value("${jwt.refresh-token-validity-seconds:1209600}") long refreshTokenValiditySeconds
    ) {
        this.objectMapper = objectMapper;
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public String createAccessToken(Long userCode) {
        return createToken(userCode, "access", accessTokenValiditySeconds);
    }

    public String createRefreshToken(Long userCode) {
        return createToken(userCode, "refresh", refreshTokenValiditySeconds);
    }

    public long getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public Long getUserCode(String token) {
        Map<String, Object> payload = parseAndValidate(token);
        return Long.valueOf(payload.get("sub").toString());
    }

    public void validateAccessToken(String token) {
        validateType(token, "access");
    }

    public void validateRefreshToken(String token) {
        validateType(token, "refresh");
    }

    private String createToken(Long userCode, String type, long validitySeconds) {
        try {
            Instant now = Instant.now();
            Map<String, Object> header = Map.of("alg", "HS256", "typ", "JWT");
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("sub", userCode.toString());
            payload.put("typ", type);
            payload.put("iat", now.getEpochSecond());
            payload.put("exp", now.plusSeconds(validitySeconds).getEpochSecond());

            String encodedHeader = encodeJson(header);
            String encodedPayload = encodeJson(payload);
            String signingInput = encodedHeader + "." + encodedPayload;
            return signingInput + "." + sign(signingInput);
        } catch (Exception e) {
            throw new IllegalStateException("JWT 생성에 실패했습니다.", e);
        }
    }

    private void validateType(String token, String expectedType) {
        Map<String, Object> payload = parseAndValidate(token);
        Object tokenType = payload.get("typ");

        if (!expectedType.equals(tokenType)) {
            throw new IllegalArgumentException("유효하지 않은 토큰 타입입니다.");
        }
    }

    private Map<String, Object> parseAndValidate(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("JWT 형식이 올바르지 않습니다.");
            }

            String signingInput = parts[0] + "." + parts[1];
            String expectedSignature = sign(signingInput);
            if (!MessageDigest.isEqual(
                    expectedSignature.getBytes(StandardCharsets.UTF_8),
                    parts[2].getBytes(StandardCharsets.UTF_8)
            )) {
                throw new IllegalArgumentException("JWT 서명이 올바르지 않습니다.");
            }

            Map<String, Object> payload = objectMapper.readValue(
                    BASE64_URL_DECODER.decode(parts[1]),
                    new TypeReference<>() {
                    }
            );
            long expiresAt = Long.parseLong(payload.get("exp").toString());
            if (Instant.now().getEpochSecond() >= expiresAt) {
                throw new IllegalArgumentException("JWT가 만료되었습니다.");
            }

            return payload;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("JWT 검증에 실패했습니다.", e);
        }
    }

    private String encodeJson(Object value) throws Exception {
        return BASE64_URL_ENCODER.encodeToString(objectMapper.writeValueAsBytes(value));
    }

    private String sign(String value) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
        return BASE64_URL_ENCODER.encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
    }
}
