package com.saram.jellylog.report.client;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeminiClient implements AiClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-pro:generateContent}")
    private String apiUrl;

    public String generateContent(String prompt) {
        String url = apiUrl + "?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        
        try {
            Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);
            return extractText(response);
        } catch (Exception e) {
            log.error("Gemini API call failed", e);
            throw new RuntimeException("AI API 호출 중 오류가 발생했습니다.", e);
        }
    }

    private String extractText(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return parts.get(0).get("text").toString().trim();
        } catch (Exception e) {
            log.error("Failed to extract text from Gemini response: {}", response, e);
            throw new RuntimeException("AI 응답에서 내용을 추출할 수 없습니다.", e);
        }
    }

    public JsonNode parseJsonResponse(String rawResponse) {
        String cleaned = cleanJson(rawResponse);
        try {
            return objectMapper.readTree(cleaned);
        } catch (Exception e) {
            log.error("Failed to parse JSON response: {}", cleaned, e);
            throw new RuntimeException("AI 응답 형식이 올바르지 않습니다.", e);
        }
    }

    private String cleanJson(String rawResponse) {
        if (rawResponse.contains("```json")) {
            rawResponse = rawResponse.substring(rawResponse.indexOf("```json") + 7);
            rawResponse = rawResponse.substring(0, rawResponse.lastIndexOf("```"));
        } else if (rawResponse.contains("```")) {
            rawResponse = rawResponse.substring(rawResponse.indexOf("```") + 3);
            rawResponse = rawResponse.substring(0, rawResponse.lastIndexOf("```"));
        }
        return rawResponse.trim();
    }
}
