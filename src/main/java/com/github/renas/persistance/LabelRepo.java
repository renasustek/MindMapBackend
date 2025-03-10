package com.github.renas.persistance;

import com.github.renas.persistance.models.GoalDao;
import com.github.renas.persistance.models.LabelDao;
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
public interface LabelRepo extends JpaRepository<LabelDao, UUID> {

    @Query("SELECT l FROM LabelDao l WHERE l.userId = :userId")
    List<LabelDao> findAllByUserId(UUID userId);

    default List<LabelDao> findAllForCurrentUser() {
        return findAllByUserId(getLoggedInUserId());
    }

    @Query("SELECT l FROM LabelDao l WHERE l.uuid = :labelId AND l.userId = :userId")
    Optional<LabelDao> findByIdAndUserId(UUID labelId,UUID userId);

    default Optional<LabelDao> findByIdUserId(UUID labelId) {
        return findByIdAndUserId(labelId,getLoggedInUserId());
    }
}
