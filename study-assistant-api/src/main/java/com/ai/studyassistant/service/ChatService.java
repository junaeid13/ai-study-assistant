package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.chat.ChatRequest;
import com.ai.studyassistant.dto.chat.ChatResponse;
import com.ai.studyassistant.dto.chat.PythonChatRequest;
import com.ai.studyassistant.repository.DocumentRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final PythonApiClient pythonApiClient;
    private final DocumentRepository documentRepository;

    public ChatService(PythonApiClient pythonApiClient, DocumentRepository documentRepository) {
        this.pythonApiClient = pythonApiClient;
        this.documentRepository = documentRepository;
    }

    public ChatResponse chat(
            Long documentId,
            ChatRequest chatRequest,
            String username
    ) {

        documentRepository.findByIdAndUsername(
                documentId,
                username
        ).orElseThrow(
                ()-> new RuntimeException(
                        "Document not found."
                )
        );


        if (documentId == null) {
            throw new IllegalArgumentException("documentId is null");
        }
        if (
                chatRequest == null ||
                        chatRequest.question() == null ||
                        chatRequest.question().isBlank()
        ) {
            throw new IllegalArgumentException("question is null or empty");
        }

        int topk = chatRequest.topK() == null ? 5 : chatRequest.topK();
        if (topk <= 0) {
            throw new IllegalArgumentException("topk is negative");
        }

        PythonChatRequest pythonChatRequest = new PythonChatRequest(
                documentId,
                chatRequest.question(),
                topk
        );

        return pythonApiClient.postForObject(
                "/chat-with-document",
                pythonChatRequest,
                ChatResponse.class
        );
    }
}
