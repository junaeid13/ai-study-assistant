package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.StudyNoteRequest;
import com.ai.studyassistant.dto.StudyNoteResponse;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.StudyNote;
import com.ai.studyassistant.mapper.StudyNoteMapper;
import com.ai.studyassistant.repository.DocumentRepository;
import com.ai.studyassistant.repository.StudyNoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@Service
@AllArgsConstructor
public class StudyNoteService {
    private final RestTemplate restTemplate;
    private final DocumentRepository documentRepository;
    private final StudyNoteRepository studyNoteRepository;
    private final StudyNoteMapper studyNoteMapper;

    @Value("${python.api.url}")
    private final String pythonApiUrl;


    public List<StudyNoteResponse> generateNotes(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (document.getNotes() != null && !document.getNotes().isEmpty()) {
            return document.getNotes().stream().map(studyNoteMapper::toResponse)
                    .toList();
        }
        // call python service
        StudyNoteRequest requestBody = new StudyNoteRequest(document.getSummary());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StudyNoteRequest> requestHttpEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<List<StudyNoteResponse>> response =
                restTemplate.exchange(
                        pythonApiUrl + "/generate-study-notes",
                        HttpMethod.POST,
                        requestHttpEntity,
                        new ParameterizedTypeReference<List<StudyNoteResponse>>() {
                        }
                );

        List<StudyNoteResponse> studyNoteResponses = response.getBody();

        if (studyNoteResponses == null || studyNoteResponses.isEmpty()) {
            throw new RuntimeException("Python service returned no study notes");
        }

        // save notes
        List<StudyNote> studyNotes = studyNoteResponses.stream()
                .map(note -> studyNoteMapper.toEntity(note, document)).toList();

        studyNoteRepository.saveAll(studyNotes);
        // return dtos
        return studyNotes.stream()
                .map(studyNoteMapper::toResponse)
                .toList();
    }
}
