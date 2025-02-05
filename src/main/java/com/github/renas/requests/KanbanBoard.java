package com.github.renas.requests;

import com.github.renas.requests.task.Task;

import java.util.List;

public record KanbanBoard (String title, String name, List<Task> todo, List<Task> inprogress, List<Task> done) {

}
