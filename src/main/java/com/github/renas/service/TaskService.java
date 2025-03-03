package com.github.renas.service;

import com.github.renas.exceptions.ResourceNotFoundException;
import com.github.renas.persistance.models.TaskDao;
import com.github.renas.requests.task.Task;
import com.github.renas.persistance.TaskRepo;
import com.github.renas.persistance.LabelRepo;
import com.github.renas.requests.task.TaskRequestForCreate;
import com.github.renas.requests.task.TaskStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TaskService {

    public final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo, LabelService labelService) {
        this.taskRepo = taskRepo;
    }

    public Task getById(UUID id) {

        return taskRepo.findById(id).map(taskDao -> new Task(
                taskDao.getId(),
                taskDao.getName(),
                taskDao.getDescription(),
                taskDao.getEisenhower(),
                taskDao.getLabelId(),
                taskDao.getCreatedDate(),
                taskDao.getDueDate(),
                taskDao.getCompletedDate(),
                taskDao.getTaskStatus()
        )).orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));
    }

    public Task create(TaskRequestForCreate task) {
        TaskDao taskDao = new TaskDao();
        taskDao.setId(UUID.randomUUID());
        taskDao.setName(task.name());
        taskDao.setDescription(task.description());
        taskDao.setEisenhower(task.eisenhowerMatrix());
        taskDao.setLabelId(task.labelId());
        taskDao.setCreatedDate(Date.valueOf(java.time.LocalDate.now()));
        taskDao.setDueDate(task.dueDate());
        taskDao.setCompletedDate(null);
        taskDao.setTaskStatus(task.taskStatus());
        TaskDao createdTask = taskRepo.save(taskDao);
        return new Task(
                createdTask.getId(),
                createdTask.getName(),
                createdTask.getDescription(),
                createdTask.getEisenhower(),
                createdTask.getLabelId(),
                createdTask.getCreatedDate(),
                createdTask.getDueDate(),
                createdTask.getCompletedDate(),
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
        taskDao.setLabelId(task.labelId());
        taskDao.setCreatedDate(task.createdDate());
        taskDao.setDueDate(task.dueDate());
        taskDao.setCompletedDate(task.completedDate());
        taskDao.setTaskStatus(task.taskStatus());
        TaskDao createdTask = taskRepo.save(taskDao);
        return new Task(
                createdTask.getId(),
                createdTask.getName(),
                createdTask.getDescription(),
                createdTask.getEisenhower(),
                createdTask.getLabelId(),
                createdTask.getCreatedDate(),
                createdTask.getDueDate(),
                createdTask.getCompletedDate(),
                createdTask.getTaskStatus()

        );
    }

    public TaskStatus changeStatus(UUID id, TaskStatus newStatus) {
        TaskDao taskDao = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));
        taskDao.setTaskStatus(newStatus);
        if(newStatus == TaskStatus.DONE) {
            taskDao.setCompletedDate(Date.valueOf(LocalDate.now()));
        } else {
            taskDao.setCompletedDate(null);
        }
        taskRepo.deleteById(id);
        return taskRepo.save(taskDao).getTaskStatus();
    }

}


