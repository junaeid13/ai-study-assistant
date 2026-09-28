package com.ai.studyassistant.dto.embedding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record EmbeddingRequest(
        @JsonProperty("document_id")
        Long documentId,
        List<String> chunks
) {
}
