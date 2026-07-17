package com.ai.studyassistant.controller;

import com.ai.studyassistant.dto.QuizResponse;
import com.ai.studyassistant.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(
            QuizService quizService
    ) {
        this.quizService = quizService;
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<List<QuizResponse>> getQuiz(
            @PathVariable Long documentId
    ) {
        return ResponseEntity.ok(
                quizService.generateQuiz(documentId)
        );
    }
}
