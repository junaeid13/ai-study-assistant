package com.ai.studyassistant.dto;

public record DocumentResponse(
        Long id,
        String filename,
        String summary,
        Long uploadedAt
) {
}