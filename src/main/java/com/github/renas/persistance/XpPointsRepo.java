package com.github.renas.persistance;

import com.github.renas.persistance.models.XpDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface XpPointsRepo extends JpaRepository<XpDao, UUID> {

}
