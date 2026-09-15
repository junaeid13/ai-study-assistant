package com.ai.studyassistant.dto.embedding;

public record EmbeddingResponse(
        String message,
        Long documentId,
        Integer chunkCount
) {
}
