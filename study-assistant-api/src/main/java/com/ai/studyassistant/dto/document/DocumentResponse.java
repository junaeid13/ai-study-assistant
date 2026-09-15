package com.ai.studyassistant.dto.document;

public record DocumentResponse(
        Long id,
        String filename,
        String summary,
        Long uploadedAt
) {
}