package com.github.renas.persistance.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "task_kanban", schema = "mind_map")
public class TaskKanbanDao {

    @Id
    @Column(name = "task_kanban_id", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID taskKanbanId;

    @Column(name = "kanban_board_uuid", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID kanbanBoardUuid;

    @Column(name = "task_uuid", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID taskUuid;

    public TaskKanbanDao() {}

    public UUID getTaskKanbanId() {
        return taskKanbanId;
    }

    public void setTaskKanbanId(UUID taskKanbanId) {
        this.taskKanbanId = taskKanbanId;
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
