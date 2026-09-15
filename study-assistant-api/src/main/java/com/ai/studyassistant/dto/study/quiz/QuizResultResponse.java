package com.ai.studyassistant.dto.study.quiz;

public record QuizResultResponse(
        int totalQuestions,
        int correctAnswers,
        double score
) {
}
