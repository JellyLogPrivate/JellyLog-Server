package com.saram.jellylog.answer.controller;

import com.saram.jellylog.answer.dto.AnswerRequest;
import com.saram.jellylog.answer.entity.Answer;
import com.saram.jellylog.answer.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answer")
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public String submitAnswer(@AuthenticationPrincipal Long userCode, @RequestBody AnswerRequest request){

        return answerService.saveAnswer(userCode, request);

    }
}
