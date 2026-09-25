package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.chat.ChatRequest;
import com.ai.studyassistant.dto.chat.ChatResponse;
import com.ai.studyassistant.dto.chat.PythonChatRequest;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.exception.DocumentNotFoundException;
import com.ai.studyassistant.exception.InvalidRequestException;
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
            throw new InvalidRequestException("Question is empty.");
        }


        int topK = chatRequest.topK() == null ? 5 : chatRequest.topK();

        if (topK < 1 || topK > 20) {
            throw new InvalidRequestException("topK must be vetween 1 and 20");
        }

        PythonChatRequest pythonChatRequest = new PythonChatRequest(
                document.getId(),
                chatRequest.question().trim(),
                topK
        );

        return pythonApiClient.postForObject(
                "/chat-with-document",
                pythonChatRequest,
                ChatResponse.class
        );
    }
}
