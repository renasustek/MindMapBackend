package com.github.renas.service;

import com.github.renas.chatbotRecources.ChatbotRequest;
import com.github.renas.chatbotRecources.ChatbotResponse;
import com.github.renas.persistance.ChatbotRepo;
import com.github.renas.persistance.models.ChatbotDao;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class ChatbotService {

    private final String CHATBOT_URL = "http://127.0.0.1:5001/chat"; // FastAPI Chatbot URL
    private final RestTemplate restTemplate = new RestTemplate();
    private final ChatbotRepo chatbotRepo;

    public ChatbotService(ChatbotRepo chatbotRepo) {
        this.chatbotRepo = chatbotRepo;
    }

    public ResponseEntity<ChatbotResponse> sendRequest(ChatbotRequest request) {
        ResponseEntity<ChatbotResponse> responseEntity = restTemplate.postForEntity(
                CHATBOT_URL, request.message(), ChatbotResponse.class
        );

        if (responseEntity.getBody() != null) {
            saveResponseToDatabase(request.message());
        }

        return responseEntity;
    }

    private void saveResponseToDatabase(String message) {
        ChatbotDao chatbotDao = new ChatbotDao();
        chatbotDao.setChatbotMessageId(UUID.randomUUID()); // Generate unique ID
        chatbotDao.setMessage(message);

        chatbotRepo.save(chatbotDao);
    }
}
