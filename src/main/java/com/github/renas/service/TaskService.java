package com.github.renas.service;

import com.github.renas.Exceptions.ResourceNotFoundException;
import com.github.renas.Request.Task.Task;
import com.github.renas.persistance.TaskRepo;
import com.github.renas.persistance.LabelRepo;
import com.github.renas.persistance.models.TaskDao;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    public final TaskRepo taskRepo;
    private final LabelRepo labelRepo;

    public TaskService(TaskRepo taskRepo, LabelRepo labelRepo) {
        this.taskRepo = taskRepo;
        this.labelRepo = labelRepo;
    }

    public Task getTaskById(UUID id) {

        return taskRepo.findById(id).map(taskDao -> new Task(
                taskDao.getId(),
                taskDao.getName(),
                taskDao.getDescription(),
                taskDao.getEisenhower(),
                taskDao.getDescription(), // You are calling getDescription twice, was this intentional?
                taskDao.getDueDate(),
                taskDao.getTaskStatus()
        )).orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));


    }
}


