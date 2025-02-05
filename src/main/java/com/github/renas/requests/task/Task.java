package com.github.renas.requests.task;


import java.util.Date;
import java.util.UUID;

public record Task(UUID id, String name, String description, EisenhowerMatrix eisenhowerMatrix, String label, Date date, TaskStatus taskStatus) {
}
