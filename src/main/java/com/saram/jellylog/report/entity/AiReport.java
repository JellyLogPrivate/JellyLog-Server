package com.saram.jellylog.report.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_report_table")
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AiReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "user_code", nullable = false)
    private Long userCode;

    @Column(name = "report_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "report_year_month", nullable = false)
    private String yearMonth; // Format: "yyyy-MM"

    @CreatedDate
    @Column(name = "report_created_at", updatable = false)
    private LocalDateTime createdAt;

    public AiReport(Long userCode, String content, String yearMonth) {
        this.userCode = userCode;
        this.content = content;
        this.yearMonth = yearMonth;
    }
}
