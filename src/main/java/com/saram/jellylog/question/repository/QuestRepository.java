package com.saram.jellylog.question.repository;

import com.saram.jellylog.question.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestRepository extends JpaRepository<Quest, Long> {
    Optional<Quest> findByQuestionCode(String questionCode);
}

