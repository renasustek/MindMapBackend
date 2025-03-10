package com.github.renas.persistance;

import com.github.renas.persistance.models.GoalDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.github.renas.security.CurrentUserId.getLoggedInUserId;

@Repository
public interface GoalRepo extends JpaRepository<GoalDao, UUID> {

    @Query("SELECT g FROM GoalDao g WHERE g.userId = :userId")
    List<GoalDao> findAllByUserId(UUID userId);

    default List<GoalDao> findAllForCurrentUser() {
        return findAllByUserId(getLoggedInUserId());
    }

    @Query("SELECT g FROM GoalDao g WHERE g.uuid = :goalId AND g.userId = :userId")
    Optional<GoalDao> findByIdAndUserId(UUID goalId, UUID userId);

    default Optional<GoalDao> findByIdUserId(UUID goalId) {
        return findByIdAndUserId(goalId,getLoggedInUserId());
    }


}
