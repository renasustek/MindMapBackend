package com.github.renas.persistance.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "task_kanban", schema = "mind_map")
public class TaskKanbanDao {

    @Id
    @Column(name = "uuid", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID uuid;

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID userId;

    @Column(name = "kanban_board_uuid", columnDefinition = "CHAR(36)", nullable = false, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID kanbanBoardUuid;

    @Column(name = "task_uuid", columnDefinition = "CHAR(36)", nullable = false, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID taskUuid;

    public TaskKanbanDao() {}

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

    public UUID getKanbanBoardUuid() {
        return kanbanBoardUuid;
    }

    public void setKanbanBoardUuid(UUID kanbanBoardUuid) {
        this.kanbanBoardUuid = kanbanBoardUuid;
    }

    public UUID getTaskUuid() {
        return taskUuid;
    }

    public void setTaskUuid(UUID taskUuid) {
        this.taskUuid = taskUuid;
    }

}
