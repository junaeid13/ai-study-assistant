package com.ai.studyassistant.controller;

import com.ai.studyassistant.dto.KeyConceptResponse;
import com.ai.studyassistant.service.KeyConceptService;
import org.springframework.http.ResponseEntity;
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
            @PathVariable("documentId") Long documentID
    ) {
        return ResponseEntity.ok(
                keyConceptService.generateKeyConcepts(documentID)
        );
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<List<KeyConceptResponse>> getKeyConcepts(
            @PathVariable("documentId") Long documentID
    ) {
        return ResponseEntity.ok(
                keyConceptService.getKeyConcepts(documentID)
        );
    }

}
