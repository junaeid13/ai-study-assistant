package com.ai.studyassistant.dto;

public record ChatRequest(
        String question,
        Integer topK
) {
}
