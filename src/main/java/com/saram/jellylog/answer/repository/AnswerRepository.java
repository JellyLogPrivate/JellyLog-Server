package com.saram.jellylog.answer.repository;

import com.saram.jellylog.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, String> {
    Optional<Answer> findTopByUserCodeOrderByAnswerCreatedAtDesc(Long userCode);
    Optional<Answer> findByUserCodeAndAnswerCreatedAtBetween(Long userCode, LocalDateTime startOfDay, LocalDateTime endOfDay);
}