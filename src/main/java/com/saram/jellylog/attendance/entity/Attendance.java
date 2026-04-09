package com.saram.jellylog.attendance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "attendence_table")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendence_code")
    private Long attendenceCode;

    @Column(name = "user_code", nullable = false)
    private Long userCode;

    @Column(name = "attendence_date", nullable = false)
    private LocalDate attendenceDate;

    public Attendance() {
    }

    public Long getAttendenceCode() {
        return attendenceCode;
    }

    public void setAttendenceCode(Long attendenceCode) {
        this.attendenceCode = attendenceCode;
    }

    public Long getUserCode() {
        return userCode;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }

    public LocalDate getAttendenceDate() {
        return attendenceDate;
    }

    public void setAttendenceDate(LocalDate attendenceDate) {
        this.attendenceDate = attendenceDate;
    }
}

