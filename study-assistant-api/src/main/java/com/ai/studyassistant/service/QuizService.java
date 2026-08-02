package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.QuizRequest;
import com.ai.studyassistant.dto.QuizResponse;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.Quiz;
import com.ai.studyassistant.repository.DocumentRepository;
import com.ai.studyassistant.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class QuizService {

    private final DocumentRepository documentRepository;
    private final QuizRepository quizRepository;
    private final PythonApiClient pythonApiClient;



    public QuizService(
            DocumentRepository documentRepository,
            QuizRepository quizRepository,
            PythonApiClient pythonApiClient
    ) {
        this.documentRepository = documentRepository;
        this.quizRepository = quizRepository;
        this.pythonApiClient = pythonApiClient;
    }

    public List<QuizResponse> generateQuiz(Long documentId) {
        // step 1 : Load Document
        Document document = documentRepository
                .findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // step 2 : If quizzes exist
        //          return them
        List<Quiz> existingQuizzes = quizRepository.findByDocumentId(documentId);

        if (!existingQuizzes.isEmpty())
            return existingQuizzes.stream()
                    .map(this::toResponse)
                    .toList();

        // step 3 : Otherwise call Python
        QuizRequest requestBody = new QuizRequest(document.getSummary());



        // step 4 : Save quiz
        List<QuizResponse> quizResponses = pythonApiClient.post(
                "/generate-quiz",
                requestBody,
                new ParameterizedTypeReference<List<QuizResponse>>() {
                }
        );

        if (quizResponses == null || quizResponses.isEmpty())
            return List.of();

        // step 5 : save quizzes into database
        List<Quiz> quizzes = quizResponses.stream()
                .map(dto -> {
                    Quiz quiz = new Quiz();

                    quiz.setQuestion(dto.question());
                    quiz.setOptionA(dto.optionA());
                    quiz.setOptionB(dto.optionB());
                    quiz.setOptionC(dto.optionC());
                    quiz.setOptionD(dto.optionD());
                    quiz.setCorrectAnswer(dto.correctAnswer());

                    quiz.setDocument(document);

                    return quiz;
                }).toList();

        List<Quiz> savedQuizzes = quizRepository.saveAll(quizzes);

        // step 6 : Return saved quizzes with generated IDs
        return savedQuizzes.stream()
                .map(this::toResponse)
                .toList();
    }

    private QuizResponse toResponse(Quiz quiz) {
        return new QuizResponse(
                quiz.getId(),
                quiz.getQuestion(),
                quiz.getOptionA(),
                quiz.getOptionB(),
                quiz.getOptionC(),
                quiz.getOptionD(),
                quiz.getCorrectAnswer()
        );
    }

}
