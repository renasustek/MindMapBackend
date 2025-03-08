package com.github.renas.persistance;
import com.github.renas.persistance.models.TaskDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepo extends JpaRepository<TaskDao, UUID> {
    @Query("SELECT t.id FROM TaskDao t WHERE t.labelId = :labelUuid")
    List<UUID> findTaskIdsByLabelId(UUID labelUuid);

    @Query("SELECT COUNT(t) FROM TaskDao t WHERE t.taskStatus = 'DONE' AND t.completedDate <= t.dueDate AND t.completedDate BETWEEN :startDate AND :endDate")
    long countTasksCompletedOnTime(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(t) FROM TaskDao t WHERE t.createdDate BETWEEN :startDate AND :endDate")
    long countTotalTasks(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}