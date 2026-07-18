package com.ai.studyassistant.dto;

import java.util.List;

public record QuizSubmissionRequest(
        Long documentId,
        List<AnswerRequest> answers
) {
}
