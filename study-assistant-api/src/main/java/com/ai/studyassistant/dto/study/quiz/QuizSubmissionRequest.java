package com.ai.studyassistant.dto.study.quiz;

import com.ai.studyassistant.dto.answer.AnswerRequest;

import java.util.List;

public record QuizSubmissionRequest(
        Long documentId,
        List<AnswerRequest> answers
) {
}
