package com.ai.studyassistant.controller;

import com.ai.studyassistant.dto.study.flashcard.FlashcardResponse;
import com.ai.studyassistant.service.FlashcardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

    private final FlashcardService flashcardService;

    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<List<FlashcardResponse>> getFlashcards(
            @PathVariable Long documentId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(
                flashcardService.generateFlashcards(documentId, username)
        );
    }
}
