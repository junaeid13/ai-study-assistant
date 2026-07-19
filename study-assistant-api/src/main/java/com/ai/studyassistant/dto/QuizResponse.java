package com.ai.studyassistant.dto;

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
