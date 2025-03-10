package com.github.renas.service;

import com.github.renas.persistance.models.TaskDao;
import com.github.renas.requests.task.Task;
import com.github.renas.persistance.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MainKanbanService {
    private final TaskPrioritisation taskPrioritisation;
    private final TaskRepo taskRepo;

    public MainKanbanService(TaskPrioritisation taskPrioritisation, TaskRepo taskRepo) {
        this.taskPrioritisation = taskPrioritisation;
        this.taskRepo = taskRepo;
    }

    public List<Task> getTaskPrioritisation() {
        Stream<Task> tasks = taskRepo.findAllTasksByUserId().stream().map(taskDao ->
                new Task(
                        taskDao.getUuid(),
                        taskDao.getName(),
                        taskDao.getDescription(),
                        taskDao.getEisenhower(),
                        taskDao.getLabelId(),
                        taskDao.getCreatedDate(),
                        taskDao.getDueDate(),
                        taskDao.getCompletedDate(),
                        taskDao.getTaskStatus()
                ));

        List<Task> sortedTasks = tasks
                .sorted(Comparator.comparingDouble(taskPrioritisation::calculatePriorityScore).reversed())
                .toList();

        return sortedTasks.stream()
                .limit(10)
                .collect(Collectors.toList());
    }
}
