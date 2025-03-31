package com.github.renas.service;

import com.github.renas.exceptions.ResourceNotFoundException;
import com.github.renas.persistance.GoalRepo;
import com.github.renas.persistance.KanbanRepo;
import com.github.renas.persistance.models.GoalDao;
import com.github.renas.persistance.models.KanbanBoardDao;
import com.github.renas.requests.GoalRequest;
import com.github.renas.responses.GoalResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class GoalServiceTest {

    @Mock
    private GoalRepo goalRepo;

    @Mock
    private KanbanRepo kanbanRepo;

    @InjectMocks
    private GoalService goalService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
    }

    @Test
    void createGoal_returnsGoalResponse() {
        GoalRequest request = new GoalRequest("Test Board", "Step 1", "Progress", true, Date.valueOf("2025-04-01"));
        KanbanBoardDao board = new KanbanBoardDao();
        board.setUuid(UUID.randomUUID());
        board.setName("Test Board");
        board.setUserId(userId);

        GoalDao goal = new GoalDao();
        goal.setUuid(UUID.randomUUID());
        goal.setUserId(userId);
        goal.setKanbanBoardId(board.getUuid());
        goal.setSpecificSteps(request.specificSteps());
        goal.setMeasureProgress(request.measureProgress());
        goal.setGoalRealistic(true);
        goal.setDueDate(request.dueDate());

        given(kanbanRepo.save(any(KanbanBoardDao.class))).willReturn(board);
        given(goalRepo.save(any(GoalDao.class))).willReturn(goal);

        GoalResponse response = goalService.createGoal(request);

        assertNotNull(response);
        assertEquals(goal.getSpecificSteps(), response.specificSteps());
        assertEquals(goal.getKanbanBoardId(), response.kanbanBoardId());
    }

    @Test
    void completeGoal_returnsUpdatedGoalResponse() {
        UUID goalId = UUID.randomUUID();
        GoalDao goal = new GoalDao();
        goal.setUuid(goalId);
        goal.setUserId(userId);
        goal.setCompletedDate(null);

        given(goalRepo.findByIdUserId(goalId)).willReturn(Optional.of(goal));
        given(goalRepo.save(any(GoalDao.class))).willReturn(goal);

        GoalResponse response = goalService.completeGoal(goalId);

        assertNotNull(response.completedDate());
    }

    @Test
    void completeGoal_throwsExceptionWhenGoalNotFound() {
        UUID goalId = UUID.randomUUID();
        given(goalRepo.findByIdUserId(goalId)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> goalService.completeGoal(goalId));
    }

    @Test
    void getAllGoals_returnsListOfGoalResponses() {
        GoalDao goal = new GoalDao();
        goal.setUuid(UUID.randomUUID());
        goal.setUserId(userId);
        goal.setSpecificSteps("Steps");
        goal.setMeasureProgress("Progress");
        goal.setGoalRealistic(true);
        goal.setDueDate(Date.valueOf("2025-04-01"));

        given(goalRepo.findAllForCurrentUser()).willReturn(List.of(goal));

        List<GoalResponse> responses = goalService.getAllGoals();

        assertEquals(1, responses.size());
        assertEquals(goal.getSpecificSteps(), responses.get(0).specificSteps());
    }
}
