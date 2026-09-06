package com.ai.studyassistant.dto;

public record SemanticSearchRequest(
        Long documentId,
        String query,
        Integer topk
) {
}
