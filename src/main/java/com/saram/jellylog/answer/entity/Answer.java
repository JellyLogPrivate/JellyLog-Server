package com.saram.jellylog.answer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "answer_table")
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Answer {

    @Id
    @Column(name = "answer_code")
    private String answerCode;

    @Column(name = "user_code", nullable = false)
    private Long userCode;

    @Column(name = "question_code", nullable = false)
    private String questionCode;

    @Column(name = "answer_content", columnDefinition = "TEXT")
    private String answerContent;

    @CreatedDate
    @Column(name = "answer_created_at", updatable = false)
    private LocalDateTime answerCreatedAt;
}