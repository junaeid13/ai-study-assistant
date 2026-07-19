package com.ai.studyassistant.dto;

public record AnswerRequest(
        Long quizId,
        String answer
) {
}
