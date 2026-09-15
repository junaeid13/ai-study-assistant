package com.ai.studyassistant.dto;

import java.util.List;

public record ChatResponse(
        String answer,
        List<SemanticSearchResponse> sources
) {
}
