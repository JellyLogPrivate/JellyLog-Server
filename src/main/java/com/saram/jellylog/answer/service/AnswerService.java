package com.saram.jellylog.answer.service;

import com.saram.jellylog.answer.dto.AnswerRequest;
import com.saram.jellylog.answer.entity.Answer;
import com.saram.jellylog.answer.repository.AnswerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    @Transactional
    public String saveAnswer(Long userCode, AnswerRequest request) {
        Answer answer = new Answer();

        answer.setAnswerCode(UUID.randomUUID().toString());
        answer.setUserCode(userCode);
        answer.setQuestionCode(request.getQuestionCode());
        answer.setAnswerContent(request.getAnswerContent());

        answerRepository.save(answer);

        return "답변이 성공적으로 저장되었습니다.";
    }
}
