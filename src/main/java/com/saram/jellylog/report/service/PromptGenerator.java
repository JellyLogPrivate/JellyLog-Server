package com.saram.jellylog.report.service;

import com.saram.jellylog.answer.entity.Answer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PromptGenerator {

    public String generateMonthlyReportPrompt(List<Answer> answers, Map<String, String> questionMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("다음은 사용자가 이번 달에 작성한 질문과 답변들이야. 이를 바탕으로 사용자의 한 달을 분석하는 리포트를 작성해 줘.\n\n");

        for (Answer answer : answers) {
            String questionContent = questionMap.getOrDefault(answer.getQuestionCode(), "질문 내용 없음");
            sb.append("질문: ").append(questionContent).append("\n");
            sb.append("답변: ").append(answer.getAnswerContent()).append("\n\n");
        }

        sb.append("\n분석 결과는 반드시 아래의 JSON 형식으로만 응답해. 다른 설명은 포함하지 마.\n");
        sb.append("{\n");
        sb.append("  \"summary\": \"한 달 요약 및 분석 내용 (최대 400자)\",\n");
        sb.append("  \"scores\": {\n");
        sb.append("    \"stability\": 0-100 점수 (정서적 안정성),\n");
        sb.append("    \"activity\": 0-100 점수 (활동 및 성실도),\n");
        sb.append("    \"happiness\": 0-100 점수 (행복감),\n");
        sb.append("    \"stress\": 0-100 점수 (스트레스 수치),\n");
        sb.append("    \"achievement\": 0-100 점수 (성취감 및 성장)\n");
        sb.append("  }\n");
        sb.append("}");

        return sb.toString();
    }
}
