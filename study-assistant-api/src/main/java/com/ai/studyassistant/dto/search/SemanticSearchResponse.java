package com.ai.studyassistant.dto.search;

public record SemanticSearchResponse(
        String content,
        Double distance,
        SearchMetadata metadata
) {
}
