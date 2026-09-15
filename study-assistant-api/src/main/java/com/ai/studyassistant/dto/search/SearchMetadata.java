package com.ai.studyassistant.dto.search;

public record SearchMetadata(
        Long documentId,
        Integer chunkIndex
) {
}
