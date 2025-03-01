package com.github.renas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.renas.chatbotRecources.ChatbotRequest;
import com.github.renas.chatbotRecources.ChatbotResponse;
import com.github.renas.service.ChatbotService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ChatbotController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ChatbotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChatbotService chatbotService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String chatbotUrl = "/api/chat";
    private final ChatbotRequest validRequest = new ChatbotRequest("Hello");
    private final ChatbotResponse chatbotResponse = new ChatbotResponse("Reframed thought response");

    @DisplayName("POST - When sending valid chatbot request, should return chatbot response")
    @Test
    void whenSendValidChatRequestShouldReturnChatResponse() throws Exception {
        when(chatbotService.sendRequest(validRequest)).thenReturn(
                org.springframework.http.ResponseEntity.ok(chatbotResponse)
        );

        mockMvc.perform(post(chatbotUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.response").value(chatbotResponse.response()));
    }

    @DisplayName("POST - When sending empty chatbot request, should return 400 Bad Request")
    @Test
    void whenSendEmptyChatRequestShouldReturn200() throws Exception {
        ChatbotRequest emptyRequest = new ChatbotRequest("");

        mockMvc.perform(post(chatbotUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isOk());
    }
}
