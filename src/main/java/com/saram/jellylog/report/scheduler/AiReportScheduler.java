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

    @Scheduled(cron = "0 0 0 1 * *")
    public void generateMonthlyReports() {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        String yearMonthStr = lastMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        log.info("Starting monthly AI report generation for: {}", yearMonthStr);

        LocalDateTime start = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime end = lastMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Long> userCodes = answerRepository.findDistinctUserCodesByAnswerCreatedAtBetween(start, end);

        for (Long userCode : userCodes) {
            try {
                aiReportService.generateMonthlyReport(userCode, yearMonthStr);
                log.info("Generated report for user: {}", userCode);
            } catch (Exception e) {
                log.error("Failed to generate report for user: {}. Error: {}", userCode, e.getMessage());
            }
        }

        log.info("Finished monthly AI report generation.");
    }
}
