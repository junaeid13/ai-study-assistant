package com.ai.studyassistant.dto;

public record PythonChatRequest(
        Long documentId,
        String question,
        Integer topK
) {
}
