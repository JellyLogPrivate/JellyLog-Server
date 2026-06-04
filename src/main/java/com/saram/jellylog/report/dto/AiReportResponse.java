package com.saram.jellylog.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiReportResponse {
    private Long id;
    private Long userCode;
    private String content;
    private String yearMonth;
    private Integer scoreStability;
    private Integer scoreActivity;
    private Integer scoreHappiness;
    private Integer scoreStress;
    private Integer scoreAchievement;
    private LocalDateTime createdAt;
}
