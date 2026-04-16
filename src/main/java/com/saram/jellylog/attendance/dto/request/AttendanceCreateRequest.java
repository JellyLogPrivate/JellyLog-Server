package com.saram.jellylog.attendance.dto.request;

import java.time.LocalDate;

public record AttendanceCreateRequest(
    Long userCode,
    LocalDate attendenceDate
) {
}

