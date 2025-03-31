package com.github.renas.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.renas.chatbotRecources.ChatbotRequest;
import com.github.renas.chatbotRecources.ChatbotResponse;
import com.github.renas.chatbotRecources.FastApiRequest;
import com.github.renas.requests.task.Label;
import com.github.renas.service.ChatbotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

@WebMvcTest(ChatbotController.class)
class ChatbotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatbotService chatbotService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getChatbotResponse_ReturnsChatbotResponse() throws Exception {
        FastApiRequest fastApiRequest = new FastApiRequest("Hello");
        Label label = new Label(UUID.randomUUID(), "Urgent");
        ChatbotRequest request = new ChatbotRequest(fastApiRequest, label);
        ChatbotResponse response = new ChatbotResponse("Hello, how can I help?");

        given(chatbotService.sendRequest(any(ChatbotRequest.class))).willReturn(ResponseEntity.ok(response));

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.response").value("Hello, how can I help?"));
    }
}
