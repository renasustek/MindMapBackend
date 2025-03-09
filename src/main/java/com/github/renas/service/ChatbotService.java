package com.github.renas.service;

import com.github.renas.chatbotRecources.ChatbotRequest;
import com.github.renas.chatbotRecources.ChatbotResponse;
import com.github.renas.gamification.Rewards;
import com.github.renas.persistance.ChatbotRepo;
import com.github.renas.persistance.models.ChatbotDao;
import com.github.renas.persistance.LabelChatbotMessageRepo;
import com.github.renas.persistance.models.LabelChatbotMessageDao;
import com.github.renas.sentimentalAnalysisRecources.SentimentalAnalysisRequest;
import com.github.renas.sentimentalAnalysisRecources.SentimentalAnalysisResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class ChatbotService {

    private final String chatbotUrl = "http://127.0.0.1:5001/chat";
    private final String sentimentalAnalysisUrl = "http://127.0.0.1:5002/analyse_sentiment";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ChatbotRepo chatbotRepo;
    private final LabelChatbotMessageRepo labelChatbotMessageRepo;
    private final XpService xpService;

    public ChatbotService(ChatbotRepo chatbotRepo, LabelChatbotMessageRepo labelChatbotMessageRepo, XpService xpService) {
        this.chatbotRepo = chatbotRepo;
        this.labelChatbotMessageRepo = labelChatbotMessageRepo;
        this.xpService = xpService;
    }

    public ResponseEntity<ChatbotResponse> sendRequest(ChatbotRequest request) {
        ResponseEntity<ChatbotResponse> chatbotResponseEntity = restTemplate.postForEntity(
                chatbotUrl, request.fastApiRequest(), ChatbotResponse.class
        );

        SentimentalAnalysisRequest sentimentalAnalysisRequest = new SentimentalAnalysisRequest(request.fastApiRequest().message());

        ResponseEntity<SentimentalAnalysisResponse> sentimentalAnalysisResponseEntity = restTemplate.postForEntity(
                sentimentalAnalysisUrl, sentimentalAnalysisRequest, SentimentalAnalysisResponse.class
        );


        if (chatbotResponseEntity.getBody() != null && sentimentalAnalysisResponseEntity.getBody() != null) {
            saveResponseToDatabase(request, sentimentalAnalysisResponseEntity.getBody().score());
        }//todo couldnt save to database if statment here

        return chatbotResponseEntity;
    }

    private void saveResponseToDatabase(ChatbotRequest request, Float sentimentScore) {
        UUID chatbotMessageId = UUID.randomUUID();

        ChatbotDao chatbotDao = new ChatbotDao();
        chatbotDao.setChatbotMessageId(chatbotMessageId);
        chatbotDao.setMessage(request.fastApiRequest().message());
        chatbotDao.setSentimentScore(sentimentScore);
        chatbotDao.setEntryDate(Date.valueOf(LocalDate.now()));
        chatbotRepo.save(chatbotDao);
        xpService.addXP(Rewards.CHATBOT_USED);

        if (request.label()!=null) {
            LabelChatbotMessageDao labelChatbotMessageDao = new LabelChatbotMessageDao();
            labelChatbotMessageDao.setId(UUID.randomUUID());
            labelChatbotMessageDao.setLabelId(request.label().uuid());
            labelChatbotMessageDao.setChatbotMessageId(chatbotMessageId);
            labelChatbotMessageRepo.save(labelChatbotMessageDao);
        }

    }
}
