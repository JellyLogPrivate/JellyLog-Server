package com.saram.jellylog.report.controller;

import com.saram.jellylog.global.ApiResponse;
import com.saram.jellylog.report.dto.AiReportResponse;
import com.saram.jellylog.report.service.AiReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class AiReportController {

    private final AiReportService aiReportService;

    @GetMapping
    public ApiResponse<List<AiReportResponse>> getReports(@AuthenticationPrincipal Long userCode) {
        return ApiResponse.success(aiReportService.getReports(userCode));
    }

    @PostMapping("/generate")
    public ApiResponse<AiReportResponse> generateReport(
            @AuthenticationPrincipal Long userCode,
            @RequestParam(required = false) String yearMonth) {
        if (yearMonth == null || yearMonth.isEmpty()) {
            yearMonth = LocalDate.now().toString();
        }
        return ApiResponse.success(aiReportService.generateMonthlyReport(userCode, yearMonth));
    }
}
