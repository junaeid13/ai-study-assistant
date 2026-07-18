package com.ai.studyassistant.controller;

import com.ai.studyassistant.dto.QuizSubmissionRequest;
import com.ai.studyassistant.service.QuizAttemptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quizzes")
public class QuizAttemptController {
    private final QuizAttemptService quizAttemptService;

    public QuizAttemptController(
            QuizAttemptService quizAttemptService
    ) {
        this.quizAttemptService = quizAttemptService;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submit(
            @RequestBody QuizSubmissionRequest quizSubmissionRequest
    ) {
        return ResponseEntity.ok(
                quizAttemptService.submitQuiz(quizSubmissionRequest)
        );
    }
}
