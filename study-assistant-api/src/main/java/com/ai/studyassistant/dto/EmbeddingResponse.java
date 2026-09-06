package com.ai.studyassistant.dto;

public record EmbeddingResponse(
        String message,
        Long documentId,
        Integer chunkCount
) {
}
