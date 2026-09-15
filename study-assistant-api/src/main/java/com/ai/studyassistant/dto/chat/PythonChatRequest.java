package com.ai.studyassistant.dto.chat;

public record PythonChatRequest(
        Long documentId,
        String question,
        Integer topK
) {
}
