package com.ai.studyassistant.service;

import com.ai.studyassistant.exception.PythonApiException;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PythonApiClient {

    private final RestTemplate restTemplate;

    @Value("${python.api.url}")
    private String pythonApiUrl;

    public PythonApiClient(
            RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    public <T, R> List<R> post(
            String endpoint,
            T requestBody,
            ParameterizedTypeReference<List<R>> responseType
    ) {

        HttpHeaders headers = createHeaders(MediaType.APPLICATION_JSON);

        HttpEntity<T> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<List<R>> response =
                restTemplate.exchange(
                        pythonApiUrl + endpoint,
                        HttpMethod.POST,
                        request,
                        responseType
                );

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new PythonApiException(
                    "Python Api returned empty response"
            );
        }
        return response.getBody();
    }

    public <T, R> R postForObject(
            String endpoint,
            T requestBody,
            Class<R> responseType
    ) {
        HttpHeaders headers = createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<T> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<R> response;
        try {


            response = restTemplate.exchange(
                    pythonApiUrl + endpoint,
                    HttpMethod.POST,
                    request,
                    responseType
            );
        } catch (HttpClientErrorException e) {
            throw new PythonApiException(
                    "Failed to communicate with python API",
                    e
            );
        }

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new PythonApiException(
                    "Python Api returned non-successful response"
            );
        }
        return response.getBody();
    }

    public String uploadFile(
            String endpoint,
            MultipartFile file
    ) {
        HttpHeaders headers = createHeaders(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body =
                new LinkedMultiValueMap<>();

        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        pythonApiUrl + endpoint,
                        request,
                        String.class
                );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new PythonApiException(
                    "Python API returned an invalid response"
            );
        }
        return response.getBody();
    }


    private HttpHeaders createHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return headers;
    }
}
