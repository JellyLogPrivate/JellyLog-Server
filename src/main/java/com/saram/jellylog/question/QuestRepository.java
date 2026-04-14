package com.saram.jellylog.question;

import com.saram.jellylog.question.dto.QuestRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestRepository extends JpaRepository<Quest, Long> {
}
