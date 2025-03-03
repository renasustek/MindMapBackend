package com.github.renas.requests.task;


import java.util.Date;
import java.util.UUID;

public record Task(UUID id, String name, String description, EisenhowerMatrix eisenhowerMatrix, UUID labelId, Date createdDate, Date dueDate, Date completedDate, TaskStatus taskStatus) {
}
