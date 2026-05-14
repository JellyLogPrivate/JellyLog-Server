package com.saram.jellylog.attendance.service;

import com.saram.jellylog.attendance.dto.request.AttendanceCreateRequest;
import com.saram.jellylog.attendance.dto.request.AttendanceUpdateRequest;
import com.saram.jellylog.attendance.dto.response.AttendanceResponse;
import com.saram.jellylog.attendance.entity.Attendance;
import com.saram.jellylog.attendance.repository.AttendanceRepository;
import com.saram.jellylog.global.exception.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendances() {
        return attendanceRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AttendanceResponse getAttendance(Long attendenceCode) {
        Attendance attendance = attendanceRepository.findById(attendenceCode)
            .orElseThrow(() -> new NotFoundException("Attendance not found."));
        return toResponse(attendance);
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getUserAttendances(Long userCode) {
        Sort sort = Sort.by(
            Sort.Order.desc("attendenceDate"),
            Sort.Order.desc("attendenceCode")
        );

        return attendanceRepository.findByUserCode(userCode, sort)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public AttendanceResponse createAttendance(AttendanceCreateRequest request) {
        Attendance attendance = new Attendance();
        attendance.setUserCode(request.userCode());
        attendance.setAttendenceDate(request.attendenceDate());

        return toResponse(attendanceRepository.save(attendance));
    }

    public AttendanceResponse updateAttendance(Long attendenceCode, AttendanceUpdateRequest request) {
        Attendance attendance = attendanceRepository.findById(attendenceCode)
            .orElseThrow(() -> new NotFoundException("Attendance not found."));

        attendance.setAttendenceDate(request.attendenceDate());
        return toResponse(attendance);
    }

    public void deleteAttendance(Long attendenceCode) {
        if (!attendanceRepository.existsById(attendenceCode)) {
            throw new NotFoundException("Attendance not found.");
        }
        attendanceRepository.deleteById(attendenceCode);
    }

    private AttendanceResponse toResponse(Attendance attendance) {
        return new AttendanceResponse(
            attendance.getAttendenceCode(),
            attendance.getUserCode(),
            attendance.getAttendenceDate()
        );
    }
}

