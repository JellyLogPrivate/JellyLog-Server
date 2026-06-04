package com.saram.jellylog.report.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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

    @Column(name = "score_stability")
    private Integer scoreStability;

    @Column(name = "score_activity")
    private Integer scoreActivity;

    @Column(name = "score_happiness")
    private Integer scoreHappiness;

    @Column(name = "score_stress")
    private Integer scoreStress;

    @Column(name = "score_achievement")
    private Integer scoreAchievement;

    @CreationTimestamp
    @Column(name = "report_created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "report_updated_at", updatable = false)
    private LocalDateTime updatedAt;

    public AiReport(Long userCode, String content, String yearMonth, Integer stability, Integer activity, Integer happiness, Integer stress, Integer achievement) {
        this.userCode = userCode;
        this.content = content;
        this.yearMonth = yearMonth;
        this.scoreStability = stability;
        this.scoreActivity = activity;
        this.scoreHappiness = happiness;
        this.scoreStress = stress;
        this.scoreAchievement = achievement;
    }
}
