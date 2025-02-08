package com.github.renas.service;

import com.github.renas.exceptions.ResourceNotFoundException;
import com.github.renas.persistance.models.TaskDao;
import com.github.renas.requests.task.Task;
import com.github.renas.persistance.TaskRepo;
import com.github.renas.persistance.LabelRepo;
import com.github.renas.requests.task.TaskRequestForCreate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskService {

    public final TaskRepo taskRepo;
    private final LabelRepo labelRepo;

    public TaskService(TaskRepo taskRepo, LabelRepo labelRepo) {
        this.taskRepo = taskRepo;
        this.labelRepo = labelRepo;
    }

    public Task getById(UUID id) {

        return taskRepo.findById(id).map(taskDao -> new Task(
                taskDao.getId(),
                taskDao.getName(),
                taskDao.getDescription(),
                taskDao.getEisenhower(),
                taskDao.getLabel(),
                taskDao.getDueDate(),
                taskDao.getTaskStatus()
        )).orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));
    }

    public Task create(TaskRequestForCreate task) {
        TaskDao taskDao = new TaskDao();
        taskDao.setId(UUID.randomUUID());
        taskDao.setName(task.name());
        taskDao.setDescription(task.description());
        taskDao.setEisenhower(task.eisenhowerMatrix());
        taskDao.setLabel(task.label());
        taskDao.setDueDate(task.date());
        taskDao.setTaskStatus(task.taskStatus());
        TaskDao createdTask = taskRepo.save(taskDao);
        return new Task(
                createdTask.getId(),
                createdTask.getName(),
                createdTask.getDescription(),
                createdTask.getEisenhower(),
                createdTask.getLabel(),
                createdTask.getDueDate(),
                createdTask.getTaskStatus()
        );
    }

    public void delete(UUID id) {
        if (!taskRepo.existsById(id)) {
            throw new ResourceNotFoundException("Task with ID " + id + " not found");
        }
        taskRepo.deleteById(id);
    }

    public Task update(Task task) {
        if (!taskRepo.existsById(task.id())) {
            throw new ResourceNotFoundException("Task with ID " + task.id() + " not found");
        }
        taskRepo.deleteById(task.id());

        TaskDao taskDao = new TaskDao();
        return saveAndConvertTaskDaoToTask(task, taskDao, task.id());
    }

    private Task saveAndConvertTaskDaoToTask(Task task, TaskDao taskDao, UUID id) {
        taskDao.setId(id);
        taskDao.setName(task.name());
        taskDao.setDescription(task.description());
        taskDao.setEisenhower(task.eisenhowerMatrix());
        taskDao.setLabel(task.label());
        taskDao.setDueDate(task.date());
        taskDao.setTaskStatus(task.taskStatus());
        TaskDao createdTask = taskRepo.save(taskDao);
        return new Task(
                createdTask.getId(),
                createdTask.getName(),
                createdTask.getDescription(),
                createdTask.getEisenhower(),
                createdTask.getLabel(),
                createdTask.getDueDate(),
                createdTask.getTaskStatus()
        );
    }
}


