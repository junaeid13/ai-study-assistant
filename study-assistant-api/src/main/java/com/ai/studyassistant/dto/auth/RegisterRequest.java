package com.ai.studyassistant.dto.auth;

public record RegisterRequest(
        String username,
        String password
) {
}
