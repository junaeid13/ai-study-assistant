package com.ai.studyassistant.controller;

import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.User;
import com.ai.studyassistant.security.JwtUtil;
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

    @GetMapping("/documents/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
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
    public ResponseEntity<Document> summarize(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file
    ) {

        String token = authHeader.substring(7);
        String username = JwtUtil.extractUsername(token);

        Document summary = documentService.summarizeFile(file, username);

        return ResponseEntity.ok(summary);
    }


    @GetMapping("/documents")
    public ResponseEntity<?> getDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }
}