package com.ai.studyassistant.dto.answer;

public record AnswerRequest(
        Long quizId,
        String answer
) {
}
