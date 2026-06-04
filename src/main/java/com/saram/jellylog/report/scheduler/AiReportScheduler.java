package com.saram.jellylog.report.scheduler;

import com.saram.jellylog.answer.repository.AnswerRepository;
import com.saram.jellylog.report.service.AiReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AiReportScheduler {

    private final AiReportService aiReportService;
    private final AnswerRepository answerRepository;

    @Scheduled(cron = "0 0/10 * * * *") // 테스트를 위해 리포트 시간 10분으로 줄임.
    public void generateMonthlyReports() {
        String yearMonthStr = java.time.YearMonth.now().toString();

        log.info("Starting AI report generation for: {}", yearMonthStr);

        LocalDateTime start = LocalDateTime.now().minusMinutes(10);
        LocalDateTime end = LocalDateTime.now();

        List<Long> userCodes = answerRepository.findDistinctUserCodesByAnswerCreatedAtBetween(start, end);

        for (Long userCode : userCodes) {
            try {
                aiReportService.generateMonthlyReport(userCode, yearMonthStr);
                log.info("Generated report for user: {}", userCode);
            } catch (Exception e) {
                log.error("Failed to generate report for user: {}. Error: {}", userCode, e.getMessage());
            }
        }

        log.info("Finished AI report generation.");
    }
}
