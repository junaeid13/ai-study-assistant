package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.AnswerRequest;
import com.ai.studyassistant.dto.QuizResultResponse;
import com.ai.studyassistant.dto.QuizSubmissionRequest;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.Quiz;
import com.ai.studyassistant.entity.QuizAttempt;
import com.ai.studyassistant.entity.User;
import com.ai.studyassistant.repository.DocumentRepository;
import com.ai.studyassistant.repository.QuizAttemptRepository;
import com.ai.studyassistant.repository.QuizRepository;
import com.ai.studyassistant.repository.UserRepository;
import com.ai.studyassistant.security.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuizAttemptService {

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private DocumentRepository documentRepository;
    private UserRepository userRepository;

    public QuizAttemptService(
            QuizRepository quizRepository,
            QuizAttemptRepository quizAttemptRepository,
            DocumentRepository documentRepository,
            UserRepository userRepository
    ) {
        this.quizRepository = quizRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    public QuizResultResponse submitQuiz(
            String authHeader,
            QuizSubmissionRequest request
    ) {

        // Step 1 : Load document
        Document document = documentRepository.findById(request.documentId())
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // Step 2 : Load quizzes
        List<Quiz> quizzes =
                quizRepository.findByDocumentId(request.documentId());

        int totalQuestions = quizzes.size();
        int correctAnswers = 0;

        // Step 3 : Compare answers

        Map<Long, Quiz> quizMap = quizzes.stream()
                .collect(Collectors.toMap(
                        Quiz::getId,
                        Function.identity()
                ));

        for (AnswerRequest answer : request.answers()) {

            Quiz quiz = quizMap.get(answer.quizId());

            if (quiz == null) {
                continue;
            }

            if (answer.answer() != null &&
                    quiz.getCorrectAnswer().equalsIgnoreCase(answer.answer())) {

                correctAnswers++;
            }
        }

        // Step 4 : Calculate score
        double score = 0;

        if (totalQuestions > 0) {

            score = ((double) correctAnswers / totalQuestions) * 100;
        }

        // Step 5 : Extract username from JWT

        String token = authHeader.substring(7);

        String username = JwtUtil.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));

        // Step 6 : Save attempt

        QuizAttempt quizAttempt = new QuizAttempt();

        quizAttempt.setUser(user);
        quizAttempt.setDocument(document);
        quizAttempt.setTotalQuestions(totalQuestions);
        quizAttempt.setCorrectAnswers(correctAnswers);
        quizAttempt.setScore(score);
        quizAttempt.setAttemptedAt(LocalDateTime.now());

        quizAttemptRepository.save(quizAttempt);

        // Step 7 : Return result

        return new QuizResultResponse(
                totalQuestions,
                correctAnswers,
                score
        );
    }
}
