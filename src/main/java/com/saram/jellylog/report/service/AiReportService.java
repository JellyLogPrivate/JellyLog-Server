package com.saram.jellylog.report.service;

import tools.jackson.databind.JsonNode;
import com.saram.jellylog.answer.entity.Answer;
import com.saram.jellylog.answer.repository.AnswerRepository;
import com.saram.jellylog.question.entity.Quest;
import com.saram.jellylog.question.repository.QuestRepository;
import com.saram.jellylog.report.client.GeminiClient;
import com.saram.jellylog.report.dto.AiReportResponse;
import com.saram.jellylog.report.entity.AiReport;
import com.saram.jellylog.report.repository.AiReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiReportService {

    private final AiReportRepository aiReportRepository;
    private final AnswerRepository answerRepository;
    private final QuestRepository questRepository;
    private final GeminiClient geminiClient;
    private final PromptGenerator promptGenerator;

    @Transactional
    public AiReportResponse generateMonthlyReport(Long userCode, String yearMonthStr) {
        return aiReportRepository.findByUserCodeAndYearMonth(userCode, yearMonthStr)
                .map(this::convertToResponse)
                .orElseGet(() -> createAndSaveReport(userCode, yearMonthStr));
    }

    private AiReportResponse createAndSaveReport(Long userCode, String yearMonthStr) {
        List<Answer> answers = fetchAnswersForMonth(userCode, yearMonthStr);
        if (answers.isEmpty()) {
            throw new RuntimeException("이 달에 작성된 답변이 없습니다.");
        }

        Map<String, String> questionMap = fetchQuestionMap(answers);
        String prompt = promptGenerator.generateMonthlyReportPrompt(answers, questionMap);
        
        try {
            String responseJson = geminiClient.generateContent(prompt);
            JsonNode root = geminiClient.parseJsonResponse(responseJson);
            
            AiReport report = buildReportFromJson(userCode, yearMonthStr, root);
            aiReportRepository.save(report);
            return convertToResponse(report);
        } catch (Exception e) {
            log.error("Failed to generate or parse AI report for user {} at {}", userCode, yearMonthStr, e);
            // Fallback: save raw response or error message if everything fails
            AiReport fallbackReport = new AiReport(userCode, "리포트 생성 중 오류가 발생했습니다: " + e.getMessage(), yearMonthStr, 0, 0, 0, 0, 0);
            aiReportRepository.save(fallbackReport);
            return convertToResponse(fallbackReport);
        }
    }

    private List<Answer> fetchAnswersForMonth(Long userCode, String yearMonthStr) {
        YearMonth yearMonth = YearMonth.parse(yearMonthStr);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        return answerRepository.findAllByUserCodeAndAnswerCreatedAtBetween(userCode, start, end);
    }

    private Map<String, String> fetchQuestionMap(List<Answer> answers) {
        Set<String> questionCodes = answers.stream()
                .map(Answer::getQuestionCode)
                .collect(Collectors.toSet());
        
        return questRepository.findAllByQuestionCodeIn(questionCodes).stream()
                .collect(Collectors.toMap(Quest::getQuestionCode, Quest::getContent, (v1, v2) -> v1));
    }

    private AiReport buildReportFromJson(Long userCode, String yearMonthStr, JsonNode root) {
        String summary = root.path("summary").asText("분석 결과를 불러올 수 없습니다.");
        JsonNode scores = root.path("scores");
        
        return new AiReport(
                userCode,
                summary,
                yearMonthStr,
                scores.path("stability").asInt(0),
                scores.path("activity").asInt(0),
                scores.path("happiness").asInt(0),
                scores.path("stress").asInt(0),
                scores.path("achievement").asInt(0)
        );
    }

    public List<AiReportResponse> getReports(Long userCode) {
        return aiReportRepository.findByUserCode(userCode).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
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
