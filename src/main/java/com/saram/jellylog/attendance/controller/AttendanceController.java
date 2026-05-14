package com.saram.jellylog.attendance.controller;

import com.saram.jellylog.attendance.dto.request.AttendanceCreateRequest;
import com.saram.jellylog.attendance.dto.request.AttendanceUpdateRequest;
import com.saram.jellylog.attendance.dto.response.AttendanceResponse;
import com.saram.jellylog.attendance.service.AttendanceService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/{attendenceCode}")
    public ResponseEntity<AttendanceResponse> getAttendance(@PathVariable Long attendenceCode) {
        return ResponseEntity.ok(attendanceService.getAttendance(attendenceCode));
    }

    @GetMapping("/users/{userCode}")
    public ResponseEntity<List<AttendanceResponse>> getUserAttendances(@PathVariable Long userCode) {
        return ResponseEntity.ok(attendanceService.getUserAttendances(userCode));
    }

    @PostMapping
    public ResponseEntity<AttendanceResponse> createAttendance(@RequestBody AttendanceCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.createAttendance(request));
    }

    @PutMapping("/{attendenceCode}")
    public ResponseEntity<AttendanceResponse> updateAttendance(
        @PathVariable Long attendenceCode,
        @RequestBody AttendanceUpdateRequest request
    ) {
        return ResponseEntity.ok(attendanceService.updateAttendance(attendenceCode, request));
    }

    @DeleteMapping("/{attendenceCode}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long attendenceCode) {
        attendanceService.deleteAttendance(attendenceCode);
        return ResponseEntity.noContent().build();
    }
}

