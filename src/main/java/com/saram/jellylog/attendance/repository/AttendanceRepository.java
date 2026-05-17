package com.saram.jellylog.attendance.repository;

import com.saram.jellylog.attendance.entity.Attendance;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByUserCode(Long userCode, Sort sort);
}

