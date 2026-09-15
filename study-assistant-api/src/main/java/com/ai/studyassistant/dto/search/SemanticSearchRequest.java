package com.ai.studyassistant.dto.search;

public record SemanticSearchRequest(
        Long documentId,
        String query,
        Integer topk
) {
}
