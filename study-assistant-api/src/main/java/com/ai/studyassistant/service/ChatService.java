package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.ChatRequest;
import com.ai.studyassistant.dto.ChatResponse;
import com.ai.studyassistant.dto.PythonChatRequest;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final PythonApiClient pythonApiClient;

    public ChatService(PythonApiClient pythonApiClient) {
        this.pythonApiClient = pythonApiClient;
    }

    public ChatResponse chat(
            Long documentId,
            ChatRequest chatRequest
    ) {
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
