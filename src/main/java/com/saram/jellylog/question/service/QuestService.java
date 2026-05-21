package com.saram.jellylog.question.service;

import com.saram.jellylog.question.repository.QuestRepository;
import com.saram.jellylog.answer.repository.AnswerRepository;
import com.saram.jellylog.answer.entity.Answer;
import com.saram.jellylog.question.entity.Quest;
import com.saram.jellylog.question.dto.QuestResponse;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestService {
    private final QuestRepository questRepository;
    private final AnswerRepository answerRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api.key}")
    private String apiKey;

    public QuestResponse getDailyQuest(Long userCode) {
        LocalDateTime lastAnswerTime = answerRepository.findTopByUserCodeOrderByAnswerCreatedAtDesc(userCode)
                .map(answer -> answer.getAnswerCreatedAt())
                .orElse(LocalDateTime.MIN);

        if (lastAnswerTime.toLocalDate().isEqual(LocalDate.now())) {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
            Optional<Answer> existingAnswer = answerRepository.findByUserCodeAndAnswerCreatedAtBetween(userCode, startOfDay, endOfDay);
            if (existingAnswer.isPresent()) {
                String questionCode = existingAnswer.get().getQuestionCode();
                Optional<Quest> existingQuest = questRepository.findByQuestionCode(questionCode);
                if (existingQuest.isPresent()) {
                    return QuestResponse.builder()
                            .questionCode(existingQuest.get().getQuestionCode())
                            .content(existingQuest.get().getContent())
                            .build();
                }
            }
            return QuestResponse.builder().questionCode(null).content("오늘의 질문에 이미 답변하셨습니다.").build(); // Fallback
        }

        Quest quest = generateAndSaveQuest();
        return QuestResponse.builder()
                .questionCode(quest.getQuestionCode())
                .content(quest.getContent())
                .build();
    }

    private Quest generateAndSaveQuest() {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", "일기 작성을 돕는 짧은 질문을 하나만 만들어줘."))))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);

        String questionText = extractText(response);
        Quest quest = new Quest(questionText, UUID.randomUUID().toString());
        questRepository.save(quest);
        return quest;
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