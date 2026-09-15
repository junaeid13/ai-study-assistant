package com.ai.studyassistant.dto.study.quiz;

public record QuizResponse(
        Long id,
        String question,
        String optionA,
        String optionB,
        String optionC,
        String optionD,
        String correctAnswer
) {
}
