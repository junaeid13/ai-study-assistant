package com.ai.studyassistant.controller;

import com.ai.studyassistant.dto.ChatRequest;
import com.ai.studyassistant.dto.ChatResponse;
import com.ai.studyassistant.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/{documentId}/chat")
    public ResponseEntity<ChatResponse> chat(
            @PathVariable Long documentId,
            @RequestBody ChatRequest chatRequest
    ) {
        return ResponseEntity.ok(
                chatService.chat(
                        documentId,
                        chatRequest
                )
        );
    }

}
