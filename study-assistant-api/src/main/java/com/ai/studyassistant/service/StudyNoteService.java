package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.study.StudyNoteRequest;
import com.ai.studyassistant.dto.study.StudyNoteResponse;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.StudyNote;
import com.ai.studyassistant.mapper.StudyNoteMapper;
import com.ai.studyassistant.repository.DocumentRepository;
import com.ai.studyassistant.repository.StudyNoteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class StudyNoteService {
    private final PythonApiClient pythonApiClient;
    private final DocumentRepository documentRepository;
    private final StudyNoteRepository studyNoteRepository;
    private final StudyNoteMapper studyNoteMapper;

    @Value("${python.api.url}")
    private String pythonApiUrl;

    public StudyNoteService(
            PythonApiClient pythonApiClient,
            DocumentRepository documentRepository,
            StudyNoteRepository studyNoteRepository,
            StudyNoteMapper studyNoteMapper
    ) {
        this.pythonApiClient = pythonApiClient;
        this.documentRepository = documentRepository;
        this.studyNoteRepository = studyNoteRepository;
        this.studyNoteMapper = studyNoteMapper;
    }


    public List<StudyNoteResponse> generateNotes(Long documentId, String username) {
        Document document = documentRepository.findByIdAndUserUsername(documentId, username)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (document.getNotes() != null && !document.getNotes().isEmpty()) {
            return document.getNotes().stream().map(studyNoteMapper::toResponse)
                    .toList();
        }
        // call python service
        StudyNoteRequest requestBody = new StudyNoteRequest(document.getSummary());

        List<StudyNoteResponse> studyNoteResponses = pythonApiClient.post(
                "/generate-study-notes",
                requestBody,
                new ParameterizedTypeReference<List<StudyNoteResponse>>() {
                }
        );

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
