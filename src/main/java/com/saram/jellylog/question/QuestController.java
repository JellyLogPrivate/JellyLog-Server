package com.saram.jellylog.question;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quest")
public class QuestController {
    private final QuestService questService;


    @PostMapping
    public Quest create(@RequestBody Quest quest) {
        return questService.create(quest);
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
