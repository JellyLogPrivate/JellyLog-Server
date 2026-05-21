package com.saram.jellylog.question.service;

import com.saram.jellylog.question.repository.QuestRepository;
import com.saram.jellylog.answer.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestService {
    private final QuestRepository questRepository;
    private final AnswerRepository answerRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api.key}")
    private String apiKey;

    public String getDailyQuest(Long userCode) {
        LocalDateTime lastAnswerTime = answerRepository.findTopByUserCodeOrderByAnswerCreatedAtDesc(userCode)
                .map(answer -> answer.getAnswerCreatedAt())
                .orElse(LocalDateTime.MIN);

        if (lastAnswerTime.toLocalDate().isEqual(LocalDate.now())) {
            return "오늘의 질문에 이미 답변하셨습니다.";
        }

        return generateAndSaveQuest();
    }

    private String generateAndSaveQuest() {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", "일기 작성을 돕는 짧은 질문을 하나만 만들어줘."))))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);

        String questionText = extractText(response);
        return questionText;
    }

    private String extractText(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return parts.get(0).get("text").toString().trim();
        } catch (Exception e) {
            return "오늘 하루는 어땠나요?";
        }
    }
}