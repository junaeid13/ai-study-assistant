package com.ai.studyassistant.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PythonChatRequest(
        @JsonProperty("document_id")
        Long documentId,
        String question,
        @JsonProperty("top_k")
        Integer topK
) {
}
