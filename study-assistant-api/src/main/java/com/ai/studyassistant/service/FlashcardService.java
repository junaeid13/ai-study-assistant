package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.FlashcardRequest;
import com.ai.studyassistant.dto.FlashcardResponse;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.repository.DocumentRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FlashcardService {

    private final DocumentRepository documentRepository;
    private final RestTemplate restTemplate;

    public FlashcardService(
            DocumentRepository documentRepository,
            RestTemplate restTemplate
    ) {
        this.documentRepository = documentRepository;
        this.restTemplate = restTemplate;
    }


    public List<FlashcardResponse> generateFlashcards(Long documentId) {


        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("document not found"));

        FlashcardRequest requestBody = new FlashcardRequest(document.getSummary());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<FlashcardRequest> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<List<FlashcardResponse>> response =
                restTemplate.exchange(
                        "http://localhost:8000/generate-flashcards",
                        HttpMethod.POST,
                        request,
                        new ParameterizedTypeReference<List<FlashcardResponse>>() {
                        }
                );

        return response.getBody();
    }
}
