package com.saram.jellylog.attendance.dto.response;

import java.time.LocalDate;

public record AttendanceResponse(
    Long attendenceCode,
    Long userCode,
    LocalDate attendenceDate
) {
}

