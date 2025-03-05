package com.github.renas.persistance;
import com.github.renas.persistance.models.TaskDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepo extends JpaRepository<TaskDao, UUID> {
    @Query("SELECT t.id FROM TaskDao t WHERE t.labelId = :labelUuid")
    List<UUID> findTaskIdsByLabelId(UUID labelUuid);
}