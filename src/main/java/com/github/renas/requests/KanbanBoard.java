package com.github.renas.requests;

import com.github.renas.requests.task.Task;

import java.util.List;
import java.util.UUID;

public record KanbanBoard (UUID id, String name, List<Task> todo, List<Task> inprogress, List<Task> done) {

}

