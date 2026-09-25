package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.chat.ChatRequest;
import com.ai.studyassistant.dto.chat.ChatResponse;
import com.ai.studyassistant.dto.chat.PythonChatRequest;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.exception.DocumentNotFoundException;
import com.ai.studyassistant.repository.DocumentRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final PythonApiClient pythonApiClient;
    private final DocumentRepository documentRepository;

    public ChatService(
            PythonApiClient pythonApiClient,
            DocumentRepository documentRepository
    ) {
        this.pythonApiClient = pythonApiClient;
        this.documentRepository = documentRepository;
    }

    public ChatResponse chat(
            Long documentId,
            ChatRequest chatRequest,
            String username
    ) {

        Document document = documentRepository.findByIdAndUserUsername(
                documentId,
                username
        ).orElseThrow(
                () -> new DocumentNotFoundException(
                        "Document not found."
                )
        );

        if (
                chatRequest.question() == null || chatRequest.question().isBlank()
        ) {
            throw new RuntimeException("Question is empty.");
        }


        int topk = chatRequest.topK() == null ? 5 : chatRequest.topK();

        if (topk < 1 || topk > 20) {
            throw new IllegalArgumentException("topk is negative");
        }

        PythonChatRequest pythonChatRequest = new PythonChatRequest(
                document.getId(),
                chatRequest.question().trim(),
                topk
        );

        return pythonApiClient.postForObject(
                "/chat-with-document",
                pythonChatRequest,
                ChatResponse.class
        );
    }
}
