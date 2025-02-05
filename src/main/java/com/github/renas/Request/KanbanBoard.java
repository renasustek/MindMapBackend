package com.github.renas.Request;

import com.github.renas.Request.Task.Task;

import java.util.List;

public record KanbanBoard (String title, String name, List<Task> todo, List<Task> inprogress, List<Task> done) {

}
