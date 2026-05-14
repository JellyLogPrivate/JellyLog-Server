package com.saram.jellylog.answer.controller;

import com.saram.jellylog.answer.dto.AnswerRequest;
import com.saram.jellylog.answer.entity.Answer;
import com.saram.jellylog.answer.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public String submitAnswer(Authentication authentication, @RequestBody AnswerRequest request){
        Long userCode = Long.valueOf(authentication.getName());

        return answerService.saveAnswer(userCode, request);

    }
}
