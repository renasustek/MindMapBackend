package com.github.renas.persistance;

import com.github.renas.persistance.models.GoalDao;
import com.github.renas.persistance.models.KanbanBoardDao;
import com.github.renas.persistance.models.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.github.renas.security.CurrentUserId.getLoggedInUserId;

@Repository
public interface KanbanRepo extends JpaRepository<KanbanBoardDao, UUID> {

    @Query("SELECT k FROM KanbanBoardDao k WHERE k.userId = :userId")
    List<KanbanBoardDao> findAllByUserId(UUID userId);

    default List<KanbanBoardDao> findAllForCurrentUser() {
        return findAllByUserId(getLoggedInUserId());
    }

    @Query("SELECT k FROM KanbanBoardDao k WHERE k.uuid = :kanbanBoardId AND k.userId = :userId")
    Optional<KanbanBoardDao> findByIdAndUserId(UUID kanbanBoardId,UUID userId);

    default Optional<KanbanBoardDao> findByIdUserId(UUID kanbanBoardId) {
        return findByIdAndUserId(kanbanBoardId,getLoggedInUserId());
    }

}
