package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.QuizResponse;
import com.ai.studyassistant.repository.DocumentRepository;
import com.ai.studyassistant.repository.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class QuizService {

    private final DocumentRepository documentRepository;
    private final QuizRepository quizRepository;
    private final RestTemplate restTemplate;

    public QuizService(
            DocumentRepository documentRepository,
            QuizRepository quizRepository,
            RestTemplate restTemplate
    ) {
        this.documentRepository = documentRepository;
        this.quizRepository = quizRepository;
        this.restTemplate = restTemplate;
    }

    public List<QuizResponse> generateQuiz(Long documentId){
        // step 1 : Load Document

        // step 2 : If quizzes exist
        //          return them

        // step 3 : Otherwise call Python

        // step 4 : Save quiz

        // step 5 : Return response

        return List.of();
    }

}
