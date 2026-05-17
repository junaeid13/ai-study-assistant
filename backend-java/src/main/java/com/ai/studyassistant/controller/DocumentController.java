package com.ai.studyassistant.controller;

import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Spring Boot is running 🚀");
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        Map<String, Object> response = new HashMap<>();

        response.put("filename", file.getOriginalFilename());
        response.put("size", file.getSize());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/summarize")
    public ResponseEntity<Document> summarize(@RequestParam("file") MultipartFile file) {

        Document summary = documentService.summarizeFile(file);

        return ResponseEntity.ok(summary);
    }


    @GetMapping("/documents")
    public ResponseEntity<?> getDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }
}