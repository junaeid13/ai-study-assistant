package com.ai.studyassistant.controller;

import com.ai.studyassistant.dto.QuizResultResponse;
import com.ai.studyassistant.dto.QuizSubmissionRequest;
import com.ai.studyassistant.service.QuizAttemptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<QuizResultResponse> submit(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody QuizSubmissionRequest request
    ) {

        QuizResultResponse response =
                quizAttemptService.submitQuiz(authHeader, request);

        return ResponseEntity.ok(response);
    }
}
