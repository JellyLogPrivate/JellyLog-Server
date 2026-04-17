package com.saram.jellylog.point.dto;

import com.saram.jellylog.point.entity.PointLogReasonType;
import java.time.LocalDate;

public record PointResponse(
    Long pointLogCode,
    Long userCode,
    Integer pointLogPointAmount,
    String pointLogReason,
    PointLogReasonType pointLogReasonType,
    LocalDate pointLogCreatedAt
) {
}
