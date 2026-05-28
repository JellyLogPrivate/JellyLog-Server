package com.saram.jellylog.report.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.saram.jellylog.answer.entity.Answer;
import com.saram.jellylog.answer.repository.AnswerRepository;
import com.saram.jellylog.question.entity.Quest;
import com.saram.jellylog.question.repository.QuestRepository;
import com.saram.jellylog.report.dto.AiReportResponse;
import com.saram.jellylog.report.entity.AiReport;
import com.saram.jellylog.report.repository.AiReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AiReportService {

    private final AiReportRepository aiReportRepository;
    private final AnswerRepository answerRepository;
    private final QuestRepository questRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        promptBuilder.append("다음은 사용자가 이번 달에 작성한 질문과 답변들이야. 이를 바탕으로 사용자의 한 달을 분석하는 리포트를 작성해 줘.\n\n");

        for (Answer answer : answers) {
            String questionContent = questRepository.findByQuestionCode(answer.getQuestionCode())
                    .map(Quest::getContent)
                    .orElse("질문 내용 없음");
            promptBuilder.append("질문: ").append(questionContent).append("\n");
            promptBuilder.append("답변: ").append(answer.getAnswerContent()).append("\n\n");
        }

        promptBuilder.append("\n분석 결과는 반드시 아래의 JSON 형식으로만 응답해. 다른 설명은 포함하지 마.\n");
        promptBuilder.append("{\n");
        promptBuilder.append("  \"summary\": \"한 달 요약 및 분석 내용 (15-25문장)\",\n");
        promptBuilder.append("  \"scores\": {\n");
        promptBuilder.append("    \"stability\": 0-100 점수 (정서적 안정성),\n");
        promptBuilder.append("    \"activity\": 0-100 점수 (활동 및 성실도),\n");
        promptBuilder.append("    \"happiness\": 0-100 점수 (행복감),\n");
        promptBuilder.append("    \"stress\": 0-100 점수 (스트레스 수치),\n");
        promptBuilder.append("    \"achievement\": 0-100 점수 (성취감 및 성장)\n");
        promptBuilder.append("  }\n");
        promptBuilder.append("}");

        String responseJson = callGemini(promptBuilder.toString());
        
        try {
            if (responseJson.contains("```json")) {
                responseJson = responseJson.substring(responseJson.indexOf("```json") + 7);
                responseJson = responseJson.substring(0, responseJson.lastIndexOf("```"));
            } else if (responseJson.contains("```")) {
                responseJson = responseJson.substring(responseJson.indexOf("```") + 3);
                responseJson = responseJson.substring(0, responseJson.lastIndexOf("```"));
            }
            
            JsonNode root = objectMapper.readTree(responseJson);
            String summary = root.path("summary").asText();
            JsonNode scores = root.path("scores");
            
            int stability = scores.path("stability").asInt(50);
            int activity = scores.path("activity").asInt(50);
            int happiness = scores.path("happiness").asInt(50);
            int stress = scores.path("stress").asInt(50);
            int achievement = scores.path("achievement").asInt(50);

            AiReport report = new AiReport(userCode, summary, yearMonthStr, stability, activity, happiness, stress, achievement);
            aiReportRepository.save(report);

            return convertToResponse(report);
        } catch (Exception e) {
            log.error("Failed to parse AI report JSON: {}", responseJson, e);
            AiReport report = new AiReport(userCode, responseJson, yearMonthStr, 0, 0, 0, 0, 0);
            aiReportRepository.save(report);
            return convertToResponse(report);
        }
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
            log.error("Gemini API call failed", e);
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
            log.error("Failed to extract text from Gemini response", e);
            return "리포트 내용을 추출할 수 없습니다.";
        }
    }

    private AiReportResponse convertToResponse(AiReport report) {
        return AiReportResponse.builder()
                .id(report.getId())
                .userCode(report.getUserCode())
                .content(report.getContent())
                .yearMonth(report.getYearMonth())
                .scoreStability(report.getScoreStability())
                .scoreActivity(report.getScoreActivity())
                .scoreHappiness(report.getScoreHappiness())
                .scoreStress(report.getScoreStress())
                .scoreAchievement(report.getScoreAchievement())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
