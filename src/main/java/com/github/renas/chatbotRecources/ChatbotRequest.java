package com.github.renas.chatbotRecources;

import com.github.renas.requests.task.Label;

public record ChatbotRequest(FastApiRequest fastApiRequest, Label label) {}