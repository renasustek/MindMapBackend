package com.github.renas.controller;

import com.github.renas.chatbotRecources.ChatbotRequest;
import com.github.renas.chatbotRecources.ChatbotResponse;
import com.github.renas.service.ChatbotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

    ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping
    public ResponseEntity<ChatbotResponse> getChatbotResponse(@RequestBody ChatbotRequest request) {
        return chatbotService.sendRequest(request);
    }
}
