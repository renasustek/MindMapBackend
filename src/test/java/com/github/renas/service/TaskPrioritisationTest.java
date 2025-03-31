package com.github.renas.service;

import com.github.renas.persistance.ChatbotRepo;
import com.github.renas.persistance.LabelChatbotMessageRepo;
import com.github.renas.persistance.TaskRepo;
import com.github.renas.requests.task.EisenhowerMatrix;
import com.github.renas.requests.task.Task;
import com.github.renas.requests.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskPrioritisationTest {

    private TaskRepo taskRepo;
    private LabelService labelService;
    private LabelChatbotMessageRepo labelChatbotRepo;
    private ChatbotRepo chatbotRepo;
    private TaskPrioritisation taskPrioritisation;

    @BeforeEach
    void setUp() {
        taskRepo = mock(TaskRepo.class);
        labelService = mock(LabelService.class);
        labelChatbotRepo = mock(LabelChatbotMessageRepo.class);
        chatbotRepo = mock(ChatbotRepo.class);
        taskPrioritisation = new TaskPrioritisation(taskRepo, labelService, labelChatbotRepo, chatbotRepo);
    }

    @Test
    void testCalculatePriorityScore_doneTask_returnsZero() {
        Task doneTask = new Task(UUID.randomUUID(), "Task 1", "Description", EisenhowerMatrix.DO,
                UUID.randomUUID(), new Date(), new Date(), null, TaskStatus.DONE);

        double score = taskPrioritisation.calculatePriorityScore(doneTask);
        assertEquals(0.0, score);
    }

    @Test
    void testCalculatePriorityScore_validTask_returnsPositiveScore() {
        UUID labelId = UUID.randomUUID();
        Date createdDate = java.sql.Date.valueOf(java.time.LocalDate.now().minusDays(5));
        Date dueDate = java.sql.Date.valueOf(java.time.LocalDate.now().plusDays(5));

        Task task = new Task(UUID.randomUUID(), "Task 1", "Description", EisenhowerMatrix.DO,
                labelId, createdDate, dueDate, null, TaskStatus.TODO);

        when(labelChatbotRepo.findChatbotIdUsingLabelIdForCurrentUser(labelId)).thenReturn(List.of(UUID.randomUUID()));
        when(chatbotRepo.findSentimentScoreForCurrentUser(any())).thenReturn(0.3f);

        double score = taskPrioritisation.calculatePriorityScore(task);
        assertTrue(score > 0);
    }

    @Test
    void testCalculatePriorityScore_emptySentiment_returnsDefaultFactor() {
        UUID labelId = UUID.randomUUID();
        Date createdDate = java.sql.Date.valueOf(java.time.LocalDate.now().minusDays(3));
        Date dueDate = java.sql.Date.valueOf(java.time.LocalDate.now().plusDays(2));

        Task task = new Task(UUID.randomUUID(), "Task 2", "Description", EisenhowerMatrix.DELEGATE,
                labelId, createdDate, dueDate, null, TaskStatus.TODO);

        when(labelChatbotRepo.findChatbotIdUsingLabelIdForCurrentUser(labelId)).thenReturn(Collections.emptyList());

        double score = taskPrioritisation.calculatePriorityScore(task);
        assertTrue(score > 0);
    }

    @Test
    void testCalculatePriorityScore_overdueTask_returnsHigherScore() {
        UUID labelId = UUID.randomUUID();
        Date createdDate = java.sql.Date.valueOf(java.time.LocalDate.now().minusDays(10));
        Date dueDate = java.sql.Date.valueOf(java.time.LocalDate.now().minusDays(2));

        Task task = new Task(UUID.randomUUID(), "Task 3", "Description", EisenhowerMatrix.DELETE,
                labelId, createdDate, dueDate, null, TaskStatus.TODO);

        when(labelChatbotRepo.findChatbotIdUsingLabelIdForCurrentUser(labelId)).thenReturn(List.of(UUID.randomUUID()));
        when(chatbotRepo.findSentimentScoreForCurrentUser(any())).thenReturn(0.5f);

        double score = taskPrioritisation.calculatePriorityScore(task);
        assertTrue(score > 0);
    }
}