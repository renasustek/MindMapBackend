package com.github.renas.persistance;
import com.github.renas.persistance.models.GoalDao;
import com.github.renas.persistance.models.LabelDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GoalRepo extends JpaRepository<GoalDao, UUID> {
}