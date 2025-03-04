package com.github.renas.responses;

import java.sql.Date;
import java.util.UUID;

public record GoalResponse(UUID uuid, UUID kanbanBoardId, String specificSteps, String measureProgress, boolean isGoalRealistic, Date dueDate, Date completedDate) {
}
