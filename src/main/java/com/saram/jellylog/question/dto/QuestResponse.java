package com.saram.jellylog.question.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestResponse {
    private String questionCode;
    private String content;
}