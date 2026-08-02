package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.FlashcardRequest;
import com.ai.studyassistant.dto.FlashcardResponse;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.Flashcard;
import com.ai.studyassistant.mapper.FlashcardMapper;
import com.ai.studyassistant.repository.DocumentRepository;
import com.ai.studyassistant.repository.FlashcardRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FlashcardService {

    private final DocumentRepository documentRepository;
    private final FlashcardMapper flashcardMapper;
    private final FlashcardRepository flashcardRepository;
    private final PythonApiClient pythonApiClient;


    public FlashcardService(
            DocumentRepository documentRepository,
            FlashcardMapper flashcardMapper,
            FlashcardRepository flashcardRepository,
            PythonApiClient pythonApiClient
    ) {
        this.documentRepository = documentRepository;
        this.flashcardMapper = flashcardMapper;
        this.flashcardRepository = flashcardRepository;
        this.pythonApiClient = pythonApiClient;
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

        List<FlashcardResponse> flashcardResponses = pythonApiClient.post(
                "/generate-flashcards",
                requestBody,
                new ParameterizedTypeReference<List<FlashcardResponse>>() {
                }
        );

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
