package com.ai.studyassistant.controller;

import com.ai.studyassistant.entity.DocumentChunk;
import com.ai.studyassistant.service.DocumentChunkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentChunkController {

    private final DocumentChunkService documentChunkService;

    public DocumentChunkController(DocumentChunkService documentChunkService) {
        this.documentChunkService = documentChunkService;
    }

    @PostMapping("/{documentId}/chunks")
    public ResponseEntity<List<DocumentChunk>> createChunks(
            @PathVariable("documentId") Long documentId
    ) {
        return ResponseEntity.ok(
                documentChunkService.createChunk(documentId)
        );
    }
}
