package com.github.renas.persistance.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "task_kanban")
public class TaskKanbanDao {

    @Id
    @Column(name = "task_kanban_id", nullable = false)
    private UUID taskKanbanId;  // Primary key field

    @Column(name = "kanban_board_uuid", nullable = false)
    private UUID kanbanBoardUuid;

    @Column(name = "task_uuid", nullable = false)
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
