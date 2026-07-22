package com.ai.studyassistant.controller;

import com.ai.studyassistant.dto.StudyNoteResponse;
import com.ai.studyassistant.service.StudyNoteService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class StudyNoteController {

    private final StudyNoteService studyNoteService;

    @PostMapping("/{documentId}/notes")
    public ResponseEntity<List<StudyNoteResponse>> generateNotes(
            @PathVariable Long documentId
    ) {
        return ResponseEntity.ok(
                studyNoteService.generateNotes(documentId)
        );
    }
}
