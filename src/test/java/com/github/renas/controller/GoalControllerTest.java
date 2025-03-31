package com.github.renas.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.renas.requests.GoalRequest;
import com.github.renas.responses.GoalResponse;
import com.github.renas.service.GoalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebMvcTest(value = GoalController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoalService goalService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createGoal_SuccessfulCreation_ReturnsGoalResponse() throws Exception {
        GoalRequest request = new GoalRequest("My Kanban Board", "Step 1, Step 2", "Weekly check-ins", true, new Date(System.currentTimeMillis()));
        GoalResponse response = new GoalResponse(
                UUID.randomUUID(), UUID.randomUUID(), "Step 1, Step 2", "Weekly check-ins", true, new Date(System.currentTimeMillis()), null);

        given(goalService.createGoal(any(GoalRequest.class))).willReturn(response);

        mockMvc.perform(post("/goal/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.specificSteps").value("Step 1, Step 2"));
    }

    @Test
    void completeGoal_GoalCompleted_ReturnsCompletedGoal() throws Exception {
        UUID goalId = UUID.randomUUID();
        GoalResponse completedGoal = new GoalResponse(goalId, UUID.randomUUID(), "Step 1, Step 2", "Weekly check-ins", true, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));

        given(goalService.completeGoal(goalId)).willReturn(completedGoal);

        mockMvc.perform(put("/goal/complete/" + goalId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.completedDate").exists());
    }

    @Test
    void getAllGoals_ReturnsAllGoals() throws Exception {
        List<GoalResponse> goals = Arrays.asList(
                new GoalResponse(UUID.randomUUID(), UUID.randomUUID(), "Step 1", "Daily review", true, new Date(System.currentTimeMillis()), null),
                new GoalResponse(UUID.randomUUID(), UUID.randomUUID(), "Step 2", "Monthly review", true, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()))
        );

        given(goalService.getAllGoals()).willReturn(goals);

        mockMvc.perform(get("/goal/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].specificSteps").value("Step 1"))
                .andExpect(jsonPath("$[1].specificSteps").value("Step 2"));
    }
}
