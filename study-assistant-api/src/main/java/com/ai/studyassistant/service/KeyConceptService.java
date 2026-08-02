package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.KeyConceptRequest;
import com.ai.studyassistant.dto.KeyConceptResponse;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.KeyConcept;
import com.ai.studyassistant.mapper.KeyConceptMapper;
import com.ai.studyassistant.repository.DocumentRepository;
import com.ai.studyassistant.repository.KeyConceptRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KeyConceptService {
    private final PythonApiClient pythonApiClient;
    private final DocumentRepository documentRepository;
    private final KeyConceptRepository keyConceptRepository;
    private final KeyConceptMapper keyConceptMapper;


    public KeyConceptService(
            DocumentRepository documentRepository,
            KeyConceptRepository keyConceptRepository,
            KeyConceptMapper keyConceptMapper,
            PythonApiClient pythonApiClient
    ) {
        this.documentRepository = documentRepository;
        this.keyConceptMapper = keyConceptMapper;
        this.keyConceptRepository = keyConceptRepository;
        this.pythonApiClient = pythonApiClient;
    }

    public List<KeyConceptResponse> generateKeyConcepts(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (document.getKeyConcepts() != null && !document.getKeyConcepts().isEmpty()) {
            return document.getKeyConcepts()
                    .stream()
                    .map(keyConceptMapper::toResponse)
                    .toList();
        }

        KeyConceptRequest requestBody = new KeyConceptRequest(document.getSummary());

        List<KeyConceptResponse> conceptResponses =
                pythonApiClient.post(
                        "/generate-key-concepts",
                        requestBody,
                        new ParameterizedTypeReference<List<KeyConceptResponse>>() {
                        }
                );

        if (conceptResponses == null || conceptResponses.isEmpty()) {
            throw new RuntimeException(
                    "Python service returned no key concepts"
            );
        }

        List<KeyConcept> concepts = conceptResponses.stream()
                .map(concept ->
                        KeyConceptMapper.toEntity(concept, document))
                .toList();

        keyConceptRepository.saveAll(concepts);


        return concepts.stream()
                .map(keyConceptMapper::toResponse)
                .toList();
    }

    public List<KeyConceptResponse> getKeyConcepts(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        return document.getKeyConcepts()
                .stream()
                .map(keyConceptMapper::toResponse)
                .toList();
    }

}
