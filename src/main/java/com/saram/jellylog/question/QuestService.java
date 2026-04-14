package com.saram.jellylog.question;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestService {
    private QuestRepository questRepository;

    public QuestService(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    public Quest create(Quest quest) {
        return questRepository.save(quest);
    }

    public List<Quest> getQuests() {
        return questRepository.findAll();
    }

    public Quest getQuestById(Long id) {
        return questRepository.findById(id).get();
    }

}
