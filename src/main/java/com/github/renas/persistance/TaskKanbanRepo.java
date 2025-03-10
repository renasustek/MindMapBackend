package com.github.renas.persistance;

import com.github.renas.persistance.models.TaskKanbanDao;
import com.github.renas.persistance.models.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

import static com.github.renas.security.CurrentUserId.getLoggedInUserId;

@Repository
public interface TaskKanbanRepo extends JpaRepository<TaskKanbanDao, UUID> {

    @Query("SELECT t.taskUuid FROM TaskKanbanDao t WHERE t.userId = :userId AND t.kanbanBoardUuid = :kanbanBoardUuid")
    List<UUID> findTaskIdsByKanbanBoardUuid(UUID kanbanBoardUuid,UUID userId);

    default List<UUID> findTaskIdsForCurrentUser(UUID kanbanBoardUuid) {
        return findTaskIdsByKanbanBoardUuid(kanbanBoardUuid, getLoggedInUserId());
    }


}
