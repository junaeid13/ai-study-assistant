package com.ai.studyassistant.dto.chat;

public record ChatRequest(
        String question,
        Integer topK
) {
}
