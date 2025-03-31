package com.github.renas.service;

import com.github.renas.chatbotRecources.ChatbotRequest;
import com.github.renas.chatbotRecources.ChatbotResponse;
import com.github.renas.chatbotRecources.FastApiRequest;
import com.github.renas.persistance.ChatbotRepo;
import com.github.renas.persistance.LabelChatbotMessageRepo;
import com.github.renas.persistance.models.ChatbotDao;
import com.github.renas.persistance.models.UserDao;
import com.github.renas.requests.task.Label;
import com.github.renas.sentimentalAnalysisRecources.SentimentalAnalysisRequest;
import com.github.renas.sentimentalAnalysisRecources.SentimentalAnalysisResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ChatbotServiceTest {

    @Mock
    private ChatbotRepo chatbotRepo;

    @Mock
    private LabelChatbotMessageRepo labelChatbotMessageRepo;

    @Mock
    private XpService xpService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ChatbotService chatbotService;

    @Captor
    private ArgumentCaptor<ChatbotDao> chatbotCaptor;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatbotService = new ChatbotService(chatbotRepo, labelChatbotMessageRepo, xpService);

        // Inject mock RestTemplate via reflection
        java.lang.reflect.Field field;
        try {
            field = ChatbotService.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(chatbotService, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock RestTemplate", e);
        }

        UserDao mockUser = new UserDao();
        UUID userId = UUID.randomUUID();
        mockUser.setId(userId);
        mockUser.setUsername("test_user");

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser); // ✅ Proper principal

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    void sendRequest_shouldCallChatbotAndSentimentApis_andSaveData() {
        // Arrange
        FastApiRequest fastApiRequest = new FastApiRequest("I'm stressed about exams");
        Label label = new Label(UUID.randomUUID(), "Academic");
        ChatbotRequest request = new ChatbotRequest(fastApiRequest, label);

        ChatbotResponse chatbotResponse = new ChatbotResponse("Try breaking it down into smaller tasks.");
        SentimentalAnalysisResponse sentimentResponse = new SentimentalAnalysisResponse(0.4f);

        when(restTemplate.postForEntity(eq("http://127.0.0.1:5001/chat"), eq(fastApiRequest), eq(ChatbotResponse.class)))
                .thenReturn(new ResponseEntity<>(chatbotResponse, HttpStatus.OK));
        when(restTemplate.postForEntity(eq("http://127.0.0.1:5002/analyse_sentiment"),
                any(SentimentalAnalysisRequest.class), eq(SentimentalAnalysisResponse.class)))
                .thenReturn(new ResponseEntity<>(sentimentResponse, HttpStatus.OK));

        ResponseEntity<ChatbotResponse> responseEntity = chatbotService.sendRequest(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Try breaking it down into smaller tasks.", responseEntity.getBody().response());

        verify(chatbotRepo, times(1)).save(any(ChatbotDao.class));
        verify(xpService, times(1)).addXP(any());
        verify(labelChatbotMessageRepo, times(1)).save(any());
    }
}
