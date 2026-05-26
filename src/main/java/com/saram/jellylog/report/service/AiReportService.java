package com.saram.jellylog.report.service;

import com.saram.jellylog.answer.entity.Answer;
import com.saram.jellylog.answer.repository.AnswerRepository;
import com.saram.jellylog.question.entity.Quest;
import com.saram.jellylog.question.repository.QuestRepository;
import com.saram.jellylog.report.dto.AiReportResponse;
import com.saram.jellylog.report.entity.AiReport;
import com.saram.jellylog.report.repository.AiReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiReportService {

    private final AiReportRepository aiReportRepository;
    private final AnswerRepository answerRepository;
    private final QuestRepository questRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api.key}")
    private String apiKey;

    @Transactional
    public AiReportResponse generateMonthlyReport(Long userCode, String yearMonthStr) {
        Optional<AiReport> existingReport = aiReportRepository.findByUserCodeAndYearMonth(userCode, yearMonthStr);
        if (existingReport.isPresent()) {
            return convertToResponse(existingReport.get());
        }

        YearMonth yearMonth = YearMonth.parse(yearMonthStr);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Answer> answers = answerRepository.findAllByUserCodeAndAnswerCreatedAtBetween(userCode, start, end);

        if (answers.isEmpty()) {
            throw new RuntimeException("이 달에 작성된 답변이 없습니다.");
        }

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("다음은 사용자가 이번 달에 작성한 질문과 답변들입니다. 이를 바탕으로 사용자의 한 달을 요약하고, 감정 변화, 주요 활동, 그리고 성장을 분석하는 리포트를 작성해 주세요.\n\n");

        for (Answer answer : answers) {
            String questionContent = questRepository.findByQuestionCode(answer.getQuestionCode())
                    .map(Quest::getContent)
                    .orElse("질문 내용 없음");
            promptBuilder.append("질문: ").append(questionContent).append("\n");
            promptBuilder.append("답변: ").append(answer.getAnswerContent()).append("\n\n");
        }

        String reportContent = callGemini(promptBuilder.toString());

        AiReport report = new AiReport(userCode, reportContent, yearMonthStr);
        aiReportRepository.save(report);

        return convertToResponse(report);
    }

    public List<AiReportResponse> getReports(Long userCode) {
        return aiReportRepository.findByUserCode(userCode).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private String callGemini(String promptText) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-pro:generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", promptText))))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        try {
            Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);
            return extractText(response);
        } catch (Exception e) {
            return "AI 리포트를 생성하는 중에 오류가 발생했습니다: " + e.getMessage();
        }
    }

    private String extractText(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return parts.get(0).get("text").toString().trim();
        } catch (Exception e) {
            return "리포트 내용을 추출할 수 없습니다.";
        }
    }

    private AiReportResponse convertToResponse(AiReport report) {
        return AiReportResponse.builder()
                .id(report.getId())
                .userCode(report.getUserCode())
                .content(report.getContent())
                .yearMonth(report.getYearMonth())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
