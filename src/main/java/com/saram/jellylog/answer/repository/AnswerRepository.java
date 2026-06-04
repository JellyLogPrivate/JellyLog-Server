package com.saram.jellylog.answer.repository;

import com.saram.jellylog.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, String> {
    Optional<Answer> findTopByUserCodeOrderByAnswerCreatedAtDesc(Long userCode);
    Optional<Answer> findByUserCodeAndAnswerCreatedAtBetween(Long userCode, LocalDateTime startOfDay, LocalDateTime endOfDay);
    java.util.List<Answer> findAllByUserCodeAndAnswerCreatedAtBetween(Long userCode, LocalDateTime start, LocalDateTime end);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT a.userCode FROM Answer a WHERE a.answerCreatedAt BETWEEN :start AND :end")
    java.util.List<Long> findDistinctUserCodesByAnswerCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByUserCodeAndQuestionCode(Long userCode, String questionCode);
}