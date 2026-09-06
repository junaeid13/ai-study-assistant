package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.SemanticSearchRequest;
import com.ai.studyassistant.dto.SemanticSearchResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemanticSearchService {

    private final PythonApiClient pythonApiClient;

    public SemanticSearchService(
            PythonApiClient pythonApiClient
    ) {
        this.pythonApiClient = pythonApiClient;
    }

    public List<SemanticSearchResponse> search(
            SemanticSearchRequest request
    ) {
        if (request.query() == null || request.query().isBlank()) {
            throw new IllegalArgumentException(
                    "Search query cannot be empty"
            );
        }

        Integer topK = request.topk() == null ? 5 : request.topk();

        if (topK <= 0) {
            throw new IllegalArgumentException(
                    "Top K must be greater than 0"
            );
        }

        SemanticSearchRequest searchRequest = new SemanticSearchRequest(
                request.documentId(),
                request.query(),
                topK
        );

        ParameterizedTypeReference<List<SemanticSearchResponse>> responseType =
                new ParameterizedTypeReference<List<SemanticSearchResponse>>() {
                };

        return pythonApiClient.post(
                "/semantic-search",
                searchRequest,
                responseType
        );
    }
}
