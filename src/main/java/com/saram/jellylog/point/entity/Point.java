package com.saram.jellylog.point.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "point_log_table")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_log_code")
    private Long pointLogCode;

    @Column(name = "user_code", nullable = false)
    private Long userCode;

    @Column(name = "point_log_point_amount", nullable = false)
    private Integer pointLogPointAmount;

    @Column(name = "point_log_reason", nullable = false, length = 50)
    private String pointLogReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_log_reason_type", nullable = false)
    private PointLogReasonType pointLogReasonType;

    @Column(name = "point_log_created_at", nullable = false)
    private LocalDate pointLogCreatedAt;

    public Point(Long userCode, Integer pointLogPointAmount, String pointLogReason, PointLogReasonType pointLogReasonType) {
        this.userCode = userCode;
        this.pointLogPointAmount = pointLogPointAmount;
        this.pointLogReason = pointLogReason;
        this.pointLogReasonType = pointLogReasonType;
    }

    public static Point create(Long userCode, Integer pointAmount, String reason, PointLogReasonType reasonType) {
        return new Point(userCode, pointAmount, reason, reasonType);
    }

    @PrePersist
    public void prePersist() {
        if (this.pointLogCreatedAt == null) {
            this.pointLogCreatedAt = LocalDate.now();
        }
    }
}
