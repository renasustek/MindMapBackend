package com.github.renas.persistance;

import com.github.renas.persistance.models.XpDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface XpPointsRepo extends JpaRepository<XpDao, UUID> {

    @Query("SELECT x FROM XpDao x ORDER BY x.xpPoints DESC")
    List<XpDao> findAllByOrderByXpDesc();
}
