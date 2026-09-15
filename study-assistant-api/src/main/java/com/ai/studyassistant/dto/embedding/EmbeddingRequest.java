package com.ai.studyassistant.dto.embedding;

import java.util.List;

public record EmbeddingRequest(
        Long documentId,
        List<String> chunks
) {
}
