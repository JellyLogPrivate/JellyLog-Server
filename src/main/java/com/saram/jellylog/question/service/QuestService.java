package com.saram.jellylog.question.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class QuestService {
    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;

    public QuestService() {
        this.restTemplate = new RestTemplate();
    }

    public String generateQuest() {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", "사용자가 일기를 쓸 수 있도록 돕는 질문을 하나만 만들어줘. 또한 단답형 질문은 하지말고 너무 단순한 질문 또한 하지마. 질문만 텍스트로 보내줘.")))));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);
            return extractText(response);
        } catch (HttpClientErrorException e) {
            System.err.println("API Error: " + e.getResponseBodyAsString());
            return "api error";
        } catch (Exception e) {
            return "server error" + e.getMessage();
        }
    }

    private String extractText(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return parts.get(0).get("text").toString().trim();
        } catch (Exception e) {
            return "오늘 하루느 어땠나요?";
        }
    }
}