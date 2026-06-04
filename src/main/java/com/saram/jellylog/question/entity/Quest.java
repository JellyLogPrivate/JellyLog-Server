package com.saram.jellylog.question.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id")
    private Long id;

    @Column(name = "question_code")
    private String questionCode;

    @Column(name = "question_order", nullable = false)
    private int questOrder = 0;

    @Column(name = "quest_content")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public Quest(String content, String questionCode) {
        this.content = content;
        this.questionCode = questionCode;
        this.createdAt = LocalDateTime.now();
        this.questOrder = 0;
    }
}
