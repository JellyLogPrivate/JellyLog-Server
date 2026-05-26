package com.saram.jellylog.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AiReportResponse {
    private Long id;
    private Long userCode;
    private String content;
    private String yearMonth;
    private LocalDateTime createdAt;
}
