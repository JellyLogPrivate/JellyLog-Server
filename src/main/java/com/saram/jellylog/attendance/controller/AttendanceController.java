package com.saram.jellylog.attendance.controller;

import com.saram.jellylog.attendance.dto.request.AttendanceCreateRequest;
import com.saram.jellylog.attendance.dto.response.AttendanceResponse;
import com.saram.jellylog.attendance.service.AttendanceService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> getAttendances() {
        return ResponseEntity.ok(attendanceService.getAttendances());
    }

    @GetMapping("/me")
    public ResponseEntity<List<AttendanceResponse>> getMyAttendances() {
        Long userCode = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(attendanceService.getUserAttendances(userCode));
    }

    @GetMapping("/{attendenceCode}")
    public ResponseEntity<AttendanceResponse> getAttendance(@PathVariable Long attendenceCode) {
        return ResponseEntity.ok(attendanceService.getAttendance(attendenceCode));
    }

    @PostMapping
    public ResponseEntity<AttendanceResponse> createAttendance(
            @RequestBody AttendanceCreateRequest request
    ) {
        Long userCode = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attendanceService.createAttendance(userCode, request));
    }
}