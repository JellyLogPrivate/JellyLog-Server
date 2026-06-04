package com.saram.jellylog.report.service;

import tools.jackson.databind.JsonNode;
import com.saram.jellylog.answer.entity.Answer;
import com.saram.jellylog.report.client.AiClient;
import com.saram.jellylog.report.dto.AiReportResponse;
import com.saram.jellylog.report.entity.AiReport;
import com.saram.jellylog.report.repository.AiReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiReportService {

    private final AiReportRepository aiReportRepository;
    private final ReportDataService reportDataService;
    private final AiClient aiClient;
    private final PromptGenerator promptGenerator;
    private final AiReportMapper aiReportMapper;

    @Transactional
    public AiReportResponse generateMonthlyReport(Long userCode, String yearMonthStr) {
        return aiReportRepository.findByUserCodeAndYearMonth(userCode, yearMonthStr)
                .map(this::convertToResponse)
                .orElseGet(() -> createAndSaveReport(userCode, yearMonthStr));
    }

    private AiReportResponse createAndSaveReport(Long userCode, String yearMonthStr) {
        List<Answer> answers = reportDataService.fetchAnswersForMonth(userCode, yearMonthStr);
        if (answers.isEmpty()) {
            throw new RuntimeException("이 달에 작성된 답변이 없습니다.");
        }

        Map<String, String> questionMap = reportDataService.fetchQuestionMap(answers);
        String prompt = promptGenerator.generateMonthlyReportPrompt(answers, questionMap);

        AiReport report;
        try {
            String responseJson = aiClient.generateContent(prompt);
            JsonNode root = aiClient.parseJsonResponse(responseJson);
            report = aiReportMapper.mapToEntity(userCode, yearMonthStr, root);
        } catch (Exception e) {
            log.error("Failed to generate or parse AI report for user {} at {}", userCode, yearMonthStr, e);
            report = aiReportMapper.createFallback(userCode, yearMonthStr, e.getMessage());
        }

        aiReportRepository.save(report);
        return convertToResponse(report);
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
