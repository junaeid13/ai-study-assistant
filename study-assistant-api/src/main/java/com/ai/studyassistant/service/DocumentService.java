package com.ai.studyassistant.service;

import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.User;
import com.ai.studyassistant.repository.DocumentRepository;
import com.ai.studyassistant.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.json.JSONObject;

import java.util.List;

@Service
public class DocumentService {

    private final RestTemplate restTemplate;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public DocumentService(
            RestTemplate restTemplate,
            DocumentRepository documentRepository,
            UserRepository userRepository
    ) {
        this.restTemplate = restTemplate;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    public Document summarizeFile(MultipartFile file) {

        String pythonUrl = "http://localhost:8000/summarize-pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                pythonUrl,
                request,
                String.class
        );


        Document document = new Document();
        document.setFilename(file.getOriginalFilename());

        String responseBody = response.getBody();

        String summary;

        try {
            JSONObject json = new JSONObject(responseBody);
            summary = json.getString("summary");
        } catch (Exception e) {
            summary = responseBody;
        }

        document.setSummary(summary);
        document.setSummary(response.getBody());
        document.setUploadedAt(System.currentTimeMillis());

        User user = userRepository.findByUsername("admin").orElseThrow();

        document.setUser(user);


        return documentRepository.save(document);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }
}