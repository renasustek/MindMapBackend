package com.github.renas.persistance;

import com.github.renas.persistance.models.TaskKanbanDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskKanbanRepo extends JpaRepository<TaskKanbanDao, UUID> {

    @Query("SELECT t.taskUuid FROM TaskKanbanDao t WHERE t.kanbanBoardUuid = :kanbanBoardUuid")
    List<UUID> findTaskIdsByKanbanBoardUuid(UUID kanbanBoardUuid);
}
