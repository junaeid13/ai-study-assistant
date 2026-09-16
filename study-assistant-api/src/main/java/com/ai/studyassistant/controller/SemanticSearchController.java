package com.ai.studyassistant.controller;

import com.ai.studyassistant.dto.search.SemanticSearchRequest;
import com.ai.studyassistant.dto.search.SemanticSearchResponse;
import com.ai.studyassistant.service.SemanticSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class SemanticSearchController {

    private final SemanticSearchService semanticSearchService;

    public SemanticSearchController(
            SemanticSearchService semanticSearchService
    ) {
        this.semanticSearchService = semanticSearchService;
    }

    @PostMapping("/{documentId}/search")
    public ResponseEntity<List<SemanticSearchResponse>> search(
            @PathVariable("documentId") Long documentId,
            @RequestBody SemanticSearchRequest request,
            Authentication authentication
    ) {

        String username = authentication.getName();
        SemanticSearchRequest searchRequest =
                new SemanticSearchRequest(
                        documentId,
                        request.query(),
                        request.topk()
                );

        return ResponseEntity.ok(semanticSearchService.search(
                documentId,
                searchRequest,
                username
        ));
    }
}
