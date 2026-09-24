package com.ai.studyassistant.controller;

import com.ai.studyassistant.dto.study.KeyConceptResponse;
import com.ai.studyassistant.service.KeyConceptService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/key-concepts")
public class KeyConceptController {

    private final KeyConceptService keyConceptService;

    public KeyConceptController(KeyConceptService keyConceptService) {
        this.keyConceptService = keyConceptService;
    }


    @PostMapping("/{documentId}")
    public ResponseEntity<List<KeyConceptResponse>> generateKeyConcepts(
            @PathVariable("documentId") Long documentID,
            Authentication authentication
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(
                keyConceptService.generateKeyConcepts(documentID, username)
        );
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<List<KeyConceptResponse>> getKeyConcepts(
            @PathVariable("documentId") Long documentID,
            Authentication authentication
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(
                keyConceptService.getKeyConcepts(documentID, username)
        );
    }

}
