package com.ai.studyassistant.dto;

public record SemanticSearchResponse(
        String content,
        Double distance,
        SearchMetadata metadata
) {
}
