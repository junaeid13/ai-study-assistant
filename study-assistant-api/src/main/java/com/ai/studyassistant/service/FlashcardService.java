package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.FlashcardRequest;
import com.ai.studyassistant.dto.FlashcardResponse;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.Flashcard;
import com.ai.studyassistant.mapper.FlashcardMapper;
import com.ai.studyassistant.repository.DocumentRepository;
import com.ai.studyassistant.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.FlashMapManager;

import java.util.List;

@Service
public class FlashcardService {

    private final DocumentRepository documentRepository;
    private final RestTemplate restTemplate;
    private final FlashcardMapper flashcardMapper;
    private final FlashcardRepository flashcardRepository;

    @Value("${python.api.url}")
    private String pythonApiUrl;

    public FlashcardService(
            DocumentRepository documentRepository,
            RestTemplate restTemplate,
            FlashcardMapper flashcardMapper,
            FlashcardRepository flashcardRepository) {
        this.documentRepository = documentRepository;
        this.restTemplate = restTemplate;
        this.flashcardMapper = flashcardMapper;
        this.flashcardRepository = flashcardRepository;
    }


    public List<FlashcardResponse> generateFlashcards(Long documentId) {


        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("document not found"));

        if (document.getFlashcards() != null &&
                !document.getFlashcards().isEmpty()
        ) {
            return document.getFlashcards()
                    .stream()
                    .map(flashcardMapper::mapToResponse)
                    .toList();
        }

        FlashcardRequest requestBody = new FlashcardRequest(document.getSummary());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<FlashcardRequest> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<List<FlashcardResponse>> response =
                restTemplate.exchange(
                        pythonApiUrl + "/generate-flashcards",
                        HttpMethod.POST,
                        request,
                        new ParameterizedTypeReference<List<FlashcardResponse>>() {
                        }
                );


        List<FlashcardResponse> flashcardResponses = response.getBody();

        if (flashcardResponses == null || flashcardResponses.isEmpty())
            throw new RuntimeException(
                    "Python service returned no flashcards"
            );

        List<Flashcard> flashcards = flashcardResponses.stream().map(
                card -> flashcardMapper.toEntity(card, document)
        ).toList();

        flashcardRepository.saveAll(flashcards);

        return flashcards.stream()
                .map(flashcardMapper::mapToResponse)
                .toList();
    }
}
