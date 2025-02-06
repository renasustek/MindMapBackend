package com.github.renas.requests.task;


import java.util.Date;
import java.util.UUID;

public record TaskRequestForCreate(String name, String description, EisenhowerMatrix eisenhowerMatrix, String label, Date date, TaskStatus taskStatus) {
}
