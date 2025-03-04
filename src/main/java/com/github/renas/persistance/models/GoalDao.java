package com.github.renas.persistance.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "goal", schema = "mind_map")
public class GoalDao {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID uuid;

    @Column(name = "kanban_board_id", columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID kanbanBoardId;

    @Column(name = "specific_steps", columnDefinition = "VARCHAR(255)", nullable = false, length = 255)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String specificSteps;

    @Column(name = "measure_progress", columnDefinition = "VARCHAR(255)", nullable = false, length = 255)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String measureProgress;

    @Column(name = "is_goal_realistic", nullable = false)
    @JdbcTypeCode(SqlTypes.BOOLEAN)
    private boolean isGoalRealistic;

    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    @JdbcTypeCode(SqlTypes.DATE)
    private Date dueDate;

    @Column(name = "completed_date", nullable = true)
    @Temporal(TemporalType.DATE)
    @JdbcTypeCode(SqlTypes.DATE)
    private Date completedDate;

    public GoalDao() {
        //needed to satisfy Hibernate
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

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}
