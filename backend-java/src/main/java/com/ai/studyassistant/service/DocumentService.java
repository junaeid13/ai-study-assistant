package com.ai.studyassistant.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {

    private final RestTemplate restTemplate;

    public DocumentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String summarizeFile(MultipartFile file) {

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

        return response.getBody();
    }
}