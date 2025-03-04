package com.github.renas.service;

import com.github.renas.exceptions.ResourceNotFoundException;
import com.github.renas.persistance.GoalRepo;
import com.github.renas.persistance.KanbanRepo;
import com.github.renas.persistance.models.GoalDao;
import com.github.renas.persistance.models.KanbanBoardDao;
import com.github.renas.requests.GoalRequest;
import com.github.renas.responses.GoalResponse;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GoalService {

    private final GoalRepo goalRepo;
    private final KanbanRepo kanbanBoardRepo;

    public GoalService(GoalRepo goalRepo, KanbanRepo kanbanBoardRepo) {
        this.goalRepo = goalRepo;
        this.kanbanBoardRepo = kanbanBoardRepo;
    }

    // Create Goal with a custom Kanban Board name
    public GoalResponse createGoal(GoalRequest request) {
        // Create Kanban Board with the provided name
        KanbanBoardDao kanbanBoard = new KanbanBoardDao();
        kanbanBoard.setUuid(UUID.randomUUID());
        kanbanBoard.setName(request.kanbanBoardName()); // Use the name from the request
        KanbanBoardDao savedKanbanBoard = kanbanBoardRepo.save(kanbanBoard);

        // Create Goal
        GoalDao goal = new GoalDao();
        goal.setUuid(UUID.randomUUID());
        goal.setKanbanBoardId(savedKanbanBoard.getUuid());
        goal.setSpecificSteps(request.specificSteps());
        goal.setMeasureProgress(request.measureProgress());
        goal.setGoalRealistic(request.isGoalRealistic());
        goal.setDueDate(request.dueDate());
        goal.setCompletedDate(null);

        GoalDao savedGoal = goalRepo.save(goal);

        return mapToGoalResponse(savedGoal);
    }

    // Complete Goal
    public GoalResponse completeGoal(UUID goalId) {
        GoalDao goal = goalRepo.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal with ID " + goalId + " not found"));

        goal.setCompletedDate(Date.valueOf(LocalDate.now())); // Set completion date to current date
        GoalDao updatedGoal = goalRepo.save(goal);

        return mapToGoalResponse(updatedGoal);
    }

    // Get all Goals
    public List<GoalResponse> getAllGoals() {
        return goalRepo.findAll().stream()
                .map(this::mapToGoalResponse)
                .collect(Collectors.toList());
    }

    private GoalResponse mapToGoalResponse(GoalDao goal) {
        return new GoalResponse(
                goal.getUuid(),
                goal.getKanbanBoardId(),
                goal.getSpecificSteps(),
                goal.getMeasureProgress(),
                goal.isGoalRealistic(),
                goal.getDueDate(),
                goal.getCompletedDate()
        );
    }
}
