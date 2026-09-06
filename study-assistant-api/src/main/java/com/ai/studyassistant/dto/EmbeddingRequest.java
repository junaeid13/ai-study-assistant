package com.ai.studyassistant.dto;

import java.util.List;

public record EmbeddingRequest(
        Long documentId,
        List<String> chunks
) {
}
