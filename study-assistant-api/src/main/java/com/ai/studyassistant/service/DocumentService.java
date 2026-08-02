package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.DocumentResponse;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.User;
import com.ai.studyassistant.repository.DocumentRepository;
import com.ai.studyassistant.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.json.JSONObject;

import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final PythonApiClient pythonApiClient;

    public DocumentService(
            DocumentRepository documentRepository,
            UserRepository userRepository,
            PythonApiClient pythonApiClient) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.pythonApiClient = pythonApiClient;
    }


    public Document summarizeFile(MultipartFile file, String username) {

        String responseBody = pythonApiClient.uploadFile(
                "/summarize-pdf",
                file
        );

        String summary;

        try {
            JSONObject json = new JSONObject(responseBody);
            summary = json.getString("summary");
        } catch (Exception e) {
            summary = responseBody;
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("jwt Username : " + username);


        Document document = new Document();
        document.setSummary(summary);
        document.setUploadedAt(System.currentTimeMillis());

        document.setUser(user);


        return documentRepository.save(document);
    }


    public List<DocumentResponse> getAllDocuments() {

        return documentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DocumentResponse getDocumentById(Long id) {

        Document document = documentRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Document not found")
                );

        return toResponse(document);
    }

    private DocumentResponse toResponse(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getFilename(),
                document.getSummary(),
                document.getUploadedAt()
        );
    }
}