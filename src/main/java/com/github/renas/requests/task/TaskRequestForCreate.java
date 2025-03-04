package com.github.renas.requests.task;


import java.util.Date;
import java.util.UUID;

public record TaskRequestForCreate(UUID kanbanboardUUID, String name, String description, EisenhowerMatrix eisenhowerMatrix, UUID labelId, Date dueDate) {}
