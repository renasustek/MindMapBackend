package com.github.renas.service;

import com.github.renas.chatbotRecources.ChatbotRequest;
import com.github.renas.chatbotRecources.ChatbotResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatbotService {

    private final String CHATBOT_URL = "http://127.0.0.1:5001/chat"; // FastAPI Chatbot URL
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<ChatbotResponse> sendRequest(ChatbotRequest request) {
        return restTemplate.postForEntity(
                CHATBOT_URL, request, ChatbotResponse.class
        );
    }
}
