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
@Table(name = "task", schema = "mind_map")
public class TaskDao {
    @Id
    @Column(name = "uuid", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @Column(name = "name", nullable = false, length = 35)
    @Size(min = 3, max = 35)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "eisenhower", nullable = false)
    private EisenhowerMatrix eisenhower;

    @Column(name = "label_id", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    private UUID labelId;

    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(name = "completed_date")
    @Temporal(TemporalType.DATE)
    private Date completedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private TaskStatus taskStatus;

    public TaskDao() {
        //needed to satisfy hibernate
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EisenhowerMatrix getEisenhower() {
        return eisenhower;
    }

    public void setEisenhower(EisenhowerMatrix eisenhower) {
        this.eisenhower = eisenhower;
    }

    public UUID getLabelId() {
        return labelId;
    }

    public void setLabelId(UUID labelId) {
        this.labelId = labelId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
}
