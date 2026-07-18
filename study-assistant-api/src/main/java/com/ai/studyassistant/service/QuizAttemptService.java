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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
            QuizSubmissionRequest request
    ) {
        //  step 1 : Load document
        Document document = documentRepository.findById(request.documentId())
                .orElseThrow(
                        () -> new RuntimeException("Document not found")
                );
        // step 2 : Load quizzes belonging to document
        List<Quiz> quizzes = quizRepository.findByDocumentId(request.documentId());
        int totalQuestions = quizzes.size();
        int correctAnswers = 0;

        // step 3 : Compare answers
        for (AnswerRequest answer : request.answers()) {
            for (Quiz quiz : quizzes) {
                if (quiz.getQuestion().equals(answer.question())) {
                    if (quiz.getCorrectAnswer().equalsIgnoreCase(answer.answer())) {
                        correctAnswers++;
                    }
                    break;
                }
            }
        }
        // step 4 : Calculate score
        double score = 0;
        if (totalQuestions > 0) {
            score = ((double) correctAnswers / totalQuestions) * 100;
        }
        // step 5 : Get current user
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Authenticated user not  found"
                        )
                );
        // step 6 : Save quiz attempt
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setUser(user);
        quizAttempt.setDocument(document);
        quizAttempt.setTotalQuestions(totalQuestions);
        quizAttempt.setCorrectAnswers(correctAnswers);
        quizAttempt.setScore(score);
        quizAttempt.setAttemptedAt(LocalDateTime.now());

        quizAttemptRepository.save(quizAttempt);
        // step 7 : Return result
        return new QuizResultResponse(
                totalQuestions,
                correctAnswers,
                score
        );
    }
}
