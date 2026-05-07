package com.saram.jellylog.point.dto;

import com.saram.jellylog.point.entity.PointLogReasonType;

public record PointRequest(
    Long userCode,
    Integer pointLogPointAmount,
    String pointLogReason,
    PointLogReasonType pointLogReasonType
) {
}
