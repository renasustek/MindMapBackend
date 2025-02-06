package com.github.renas.persistance.models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "goal")
public class GoalDao {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "kanban_board_id", nullable = false, updatable = false)
    private UUID kanbanBoardId;

    @Column(name = "specific_steps", columnDefinition = "VARCHAR(255)", nullable = false, unique = false, length = 255)
    private String specificSteps;

    @Column(name = "measure_progress", columnDefinition = "VARCHAR(255)", nullable = false, unique = false, length = 255)
    private String measureProgress;

    @Column(name = "is_goal_realistic", columnDefinition = "VARCHAR(255)", nullable = false, unique = false, length = 255)
    private boolean isGoalRealistic;

    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    public GoalDao() {
        //needed to satisfy hibernate
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getKanbanBoardId() {
        return kanbanBoardId;
    }

    public void setKanbanBoardId(UUID kanbanBoardId) {
        this.kanbanBoardId = kanbanBoardId;
    }

    public String getSpecificSteps() {
        return specificSteps;
    }

    public void setSpecificSteps(String specificSteps) {
        this.specificSteps = specificSteps;
    }

    public String getMeasureProgress() {
        return measureProgress;
    }

    public void setMeasureProgress(String measureProgress) {
        this.measureProgress = measureProgress;
    }

    public boolean isGoalRealistic() {
        return isGoalRealistic;
    }

    public void setGoalRealistic(boolean goalRealistic) {
        isGoalRealistic = goalRealistic;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
