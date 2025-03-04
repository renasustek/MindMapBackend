package com.github.renas.requests;

import java.sql.Date;

public record GoalRequest(String kanbanBoardName, String specificSteps, String measureProgress, boolean isGoalRealistic, Date dueDate) {
}
