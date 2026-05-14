package com.saram.jellylog.question.repository;

import com.saram.jellylog.question.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestRepository extends JpaRepository<Quest, Long> {
}
