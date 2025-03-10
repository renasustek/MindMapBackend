package com.github.renas.persistance;

import com.github.renas.persistance.models.TaskDao;
import com.github.renas.persistance.models.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.github.renas.security.CurrentUserId.getLoggedInUserId;

@Repository
public interface TaskRepo extends JpaRepository<TaskDao, UUID> {

    @Query("SELECT t.uuid FROM TaskDao t WHERE t.labelId = :labelUuid AND t.userId = :userId")
    List<UUID> findTaskIdsByLabelId(UUID labelUuid, UUID userId);

    default List<UUID> findTaskIdsByLabelIdForCurrentUser(UUID labelUuid) {
        return findTaskIdsByLabelId(labelUuid, getLoggedInUserId());
    }

    @Query("SELECT t FROM TaskDao t WHERE t.uuid IN :taskIds AND t.userId = :userId")
    List<TaskDao> findAllByTaskIdsForUser(List<UUID> taskIds, UUID userId);

    default List<TaskDao> findAllForCurrentUserByTaskIds(List<UUID> taskIds) {
        return findAllByTaskIdsForUser(taskIds, getLoggedInUserId());
    }

    @Query("SELECT t FROM TaskDao t WHERE t.uuid = :uuid AND t.userId = :userId")
    Optional<TaskDao> findTaskById(UUID uuid,UUID userId);

    default Optional<TaskDao> findTaskByIdForCurrentUser(UUID uuid) {
        return findTaskById(uuid, getLoggedInUserId());
    }

    @Query("SELECT t FROM TaskDao t WHERE t.userId = :userId")
    List<TaskDao> findByIdAndUserId(UUID userId);

    default List<TaskDao> findAllTasksByUserId() {
        return findByIdAndUserId(getLoggedInUserId());
    }


    @Query("SELECT COUNT(t) FROM TaskDao t WHERE t.taskStatus = 'DONE' " +
            "AND t.completedDate <= t.dueDate " +
            "AND t.completedDate BETWEEN :startDate AND :endDate " +
            "AND t.userId = :userId")
    long countTasksCompletedOnTime(@Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate,
                                   @Param("userId") UUID userId);

    default long countTasksCompletedOnTimeForCurrentUser(LocalDate startDate, LocalDate endDate) {
        return countTasksCompletedOnTime(startDate, endDate, getLoggedInUserId());
    }

    @Query("SELECT COUNT(t) FROM TaskDao t WHERE t.createdDate BETWEEN :startDate AND :endDate " +
            "AND t.userId = :userId")
    long countTotalTasks(@Param("startDate") LocalDate startDate,
                         @Param("endDate") LocalDate endDate,
                         @Param("userId") UUID userId);

    default long countTotalTasksForCurrentUser(LocalDate startDate, LocalDate endDate) {
        return countTotalTasks(startDate, endDate, getLoggedInUserId());
    }


}
