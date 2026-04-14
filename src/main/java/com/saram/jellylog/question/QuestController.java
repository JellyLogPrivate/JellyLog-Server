package com.saram.jellylog.question;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestController {
    private QuestService questService;

    private void setQuestService(QuestService questService) {
        this.questService = questService;
    }

    @GetMapping
    public List<Quest> getQuests() {
        return questService.getQuests();
    }

    @GetMapping("/{id}")
    public Quest getQuestById(@PathVariable Long id) {
        return questService.getQuestById(id);
    }
}
