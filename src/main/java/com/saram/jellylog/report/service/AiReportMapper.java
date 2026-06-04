package com.saram.jellylog.report.service;

import com.saram.jellylog.report.entity.AiReport;
import tools.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class AiReportMapper {

    public AiReport mapToEntity(Long userCode, String yearMonthStr, JsonNode root) {
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

    public AiReport createFallback(Long userCode, String yearMonthStr, String errorMessage) {
        return new AiReport(
                userCode,
                "리포트 생성 중 오류가 발생했습니다: " + errorMessage,
                yearMonthStr,
                0, 0, 0, 0, 0
        );
    }
}
