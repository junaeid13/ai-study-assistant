package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.StudyNoteResponse;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.repository.DocumentRepository;
import com.ai.studyassistant.repository.StudyNoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class StudyNoteService {
    private final RestTemplate restTemplate;
    private final DocumentRepository documentRepository;
    private final StudyNoteRepository studyNoteRepository;


    public List<StudyNoteResponse> generateNotes(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // call python service
        // save notes
        // return dtos
        return null;
    }
}
