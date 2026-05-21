package com.saram.jellylog.question.controller;

import com.saram.jellylog.question.dto.QuestResponse;
import com.saram.jellylog.question.service.QuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quest")
public class QuestController {
    private final QuestService questService;

    @GetMapping("/generate")
    public QuestResponse generateQuest(@AuthenticationPrincipal Long userCode) {
        return questService.getDailyQuest(userCode);
    }

//    @PostMapping
//    public Quest create(@RequestBody Quest quest) {
//        return questService.create(quest);
//    }
//
//    @GetMapping
//    public List<Quest> getQuests() {
//        return questService.getQuests();
//    }
//
//    @GetMapping("/{id}")
//    public Quest getQuestById(@PathVariable Long id) {
//        return questService.getQuestById(id);
//    }
}