package com.github.renas.persistance.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "goal", schema = "mind_map")
public class GoalDao {

    @Id
    @Column(name = "uuid", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID uuid;

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID userId;

    @Column(name = "kanban_board_id", columnDefinition = "CHAR(36)",nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID kanbanBoardId;

    @Column(name = "specific_steps", length = 255)
    private String specificSteps;

    @Column(name = "measure_progress", length = 255)
    private String measureProgress;

    @Column(name = "is_goal_realistic", nullable = false)
    private boolean isGoalRealistic;

    @Temporal(TemporalType.DATE)
    @Column(name = "due_date")
    private Date dueDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "completed_date")
    private Date completedDate;

    public GoalDao() {}

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}
