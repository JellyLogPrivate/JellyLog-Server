package com.saram.jellylog.answer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerRequest {
    private String questionCode;
    private String answerContent;
}
