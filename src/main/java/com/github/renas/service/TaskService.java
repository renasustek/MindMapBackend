package com.github.renas.service;

import com.github.renas.exceptions.ResourceNotFoundException;
import com.github.renas.gamification.Rewards;
import com.github.renas.persistance.TaskKanbanRepo;
import com.github.renas.persistance.models.TaskDao;
import com.github.renas.persistance.models.TaskKanbanDao;
import com.github.renas.requests.task.Task;
import com.github.renas.persistance.TaskRepo;
import com.github.renas.requests.task.TaskRequestForCreate;
import com.github.renas.requests.task.TaskStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import static com.github.renas.security.CurrentUserId.getLoggedInUserId;

@Service
public class TaskService {

    private final TaskRepo taskRepo;
    private final TaskKanbanRepo taskKanbanRepo;
    private final XpService xpService;

    public TaskService(TaskRepo taskRepo, LabelService labelService, TaskKanbanRepo taskKanbanRepo, XpService xpService) {
        this.taskRepo = taskRepo;
        this.taskKanbanRepo = taskKanbanRepo;
        this.xpService = xpService;
    }

    public Task getById(UUID id) {

        return taskRepo.findTaskByIdForCurrentUser(id).map(taskDao -> new Task(
                taskDao.getUuid(),
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

    @Transactional
    public Task create(TaskRequestForCreate task) {
        TaskDao taskDao = new TaskDao();

        taskDao.setUuid(UUID.randomUUID());
        taskDao.setUserId(getLoggedInUserId());
        taskDao.setName(task.name());
        taskDao.setDescription(task.description());
        taskDao.setEisenhower(task.eisenhowerMatrix());
        taskDao.setLabelId(task.labelId());
        taskDao.setCreatedDate(Date.valueOf(java.time.LocalDate.now()));
        taskDao.setDueDate(task.dueDate());
        taskDao.setCompletedDate(null);
        taskDao.setTaskStatus(TaskStatus.TODO);

        TaskDao createdTask = taskRepo.save(taskDao);

        TaskKanbanDao taskKanbanDao = new TaskKanbanDao();

        taskKanbanDao.setKanbanBoardUuid(UUID.randomUUID());
        taskKanbanDao.setTaskUuid(createdTask.getUuid());
        taskKanbanDao.setKanbanBoardUuid(task.kanbanboardUUID());
        taskKanbanRepo.save(taskKanbanDao);

        return new Task(
                createdTask.getUuid(),
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
    }//todo make it delete by user id aswell

    public Task update(Task task) {
        if (!taskRepo.existsById((task.id()))) {
            throw new ResourceNotFoundException("Task with ID " + task.id() + " not found");
        }
        taskRepo.deleteById(task.id());

        TaskDao taskDao = new TaskDao();
        return saveAndConvertTaskDaoToTask(task, taskDao, task.id());
    }//todo when you delete and save it it deletes all forigen keys-> make it just update it not delete and put back
    //todo make it update by user id

    private Task saveAndConvertTaskDaoToTask(Task task, TaskDao taskDao, UUID id) {
        taskDao.setUuid(id);
        taskDao.setUserId(getLoggedInUserId());
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
                createdTask.getUuid(),
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

        TaskDao taskDao = taskRepo.findTaskByIdForCurrentUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with ID " + id + " not found"));

        taskDao.setTaskStatus(newStatus);

        if (newStatus == TaskStatus.DONE) {
            taskDao.setCompletedDate(Date.valueOf(LocalDate.now()));
            xpService.addXP(Rewards.TASK_COMPLETED);
        } else {
            taskDao.setCompletedDate(null);
        }

        return taskRepo.save(taskDao).getTaskStatus();
    }

}


