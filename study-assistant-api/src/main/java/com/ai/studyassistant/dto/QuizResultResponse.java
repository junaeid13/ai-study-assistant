package com.ai.studyassistant.dto;

public record QuizResultResponse(
        int totalQuestions,
        int correctAnswers,
        double score
) {
}
