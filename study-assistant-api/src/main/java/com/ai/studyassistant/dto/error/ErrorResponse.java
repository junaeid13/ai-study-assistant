package com.ai.studyassistant.dto.error;

public record ErrorResponse(
        int status,
        String message
) {
}
