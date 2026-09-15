package com.ai.studyassistant.dto.auth;

public record LoginRequest(
        String username,
        String password
) {
}
