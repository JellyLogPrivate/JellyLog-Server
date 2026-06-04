package com.saram.jellylog.report.service;

import com.saram.jellylog.answer.entity.Answer;
import com.saram.jellylog.answer.repository.AnswerRepository;
import com.saram.jellylog.question.entity.Quest;
import com.saram.jellylog.question.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportDataService {

    private final AnswerRepository answerRepository;
    private final QuestRepository questRepository;

    public List<Answer> fetchAnswersForMonth(Long userCode, String yearMonthStr) {
        YearMonth yearMonth = YearMonth.parse(yearMonthStr);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        return answerRepository.findAllByUserCodeAndAnswerCreatedAtBetween(userCode, start, end);
    }

    public Map<String, String> fetchQuestionMap(List<Answer> answers) {
        Set<String> questionCodes = answers.stream()
                .map(Answer::getQuestionCode)
                .collect(Collectors.toSet());

        return questRepository.findAllByQuestionCodeIn(questionCodes).stream()
                .collect(Collectors.toMap(Quest::getQuestionCode, Quest::getContent, (v1, v2) -> v1));
    }
}
