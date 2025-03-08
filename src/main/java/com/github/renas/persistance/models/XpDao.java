package com.github.renas.persistance.models;

import com.github.renas.requests.task.EisenhowerMatrix;
import com.github.renas.requests.task.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;
import java.util.Date;

@Entity
@Table(name = "xp_points", schema = "mind_map")
public class XpDao {
    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @Column(name = "xp_points", nullable = false)
    private int xpPoints;

    public XpDao() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getXpPoints() {
        return xpPoints;
    }

    public void setXpPoints(int xpPoints) {
        this.xpPoints = xpPoints;
    }
}
