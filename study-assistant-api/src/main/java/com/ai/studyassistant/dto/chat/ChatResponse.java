package com.ai.studyassistant.dto.chat;

import com.ai.studyassistant.dto.search.SemanticSearchResponse;

import java.util.List;

public record ChatResponse(
        String answer,
        List<SemanticSearchResponse> sources
) {
}
