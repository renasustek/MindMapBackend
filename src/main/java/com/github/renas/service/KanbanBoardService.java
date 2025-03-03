package com.github.renas.service;


import com.github.renas.exceptions.ResourceNotFoundException;
import com.github.renas.persistance.KanbanRepo;
import com.github.renas.persistance.TaskKanbanRepo;
import com.github.renas.persistance.TaskRepo;
import com.github.renas.persistance.models.KanbanBoardDao;
import com.github.renas.persistance.models.TaskDao;
import com.github.renas.requests.KanbanBoard;
import com.github.renas.requests.task.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class KanbanBoardService {

    private KanbanRepo kanbanRepo;
    private TaskRepo taskRepo;
    private TaskKanbanRepo taskKanbanRepo;

    public KanbanBoardService(KanbanRepo kanbanRepo, TaskRepo taskRepo, TaskKanbanRepo taskKanbanRepo) {
        this.kanbanRepo = kanbanRepo;
        this.taskRepo = taskRepo;
        this.taskKanbanRepo = taskKanbanRepo;
    }


    public KanbanBoard getKanbanBoard(UUID id) {
        String boardName = getKanbanBoardName(id);

        List<UUID> taskUUIDs = taskKanbanRepo.findTaskIdsByKanbanBoardUuid(id);

        if (taskUUIDs.isEmpty()) {
            throw new ResourceNotFoundException("Kanbanboard with ID " + id + " has no tasks");
        }

        List<TaskDao> taskDaos = taskRepo.findAllById(taskUUIDs);

        List<Task> tasksList = taskDaos.stream().map(taskDao -> new Task(
                        taskDao.getId(),
                        taskDao.getName(),
                        taskDao.getDescription(),
                        taskDao.getEisenhower(),
                        taskDao.getLabelId(),
                        taskDao.getCreatedDate(),
                        taskDao.getDueDate(),
                        taskDao.getCompletedDate(),
                        taskDao.getTaskStatus()
                )
        ).toList();//todo call the service not the repo please - no need to redo logic

        List<Task> todoTasks = new ArrayList<>();
        List<Task> inProgressTasks = new ArrayList<>();
        List<Task> doneTasks = new ArrayList<>();

        for (Task task : tasksList) {
            switch (task.taskStatus()) {
                case TODO:
                    todoTasks.add(task);
                    break;
                case INPROGRESS:
                    inProgressTasks.add(task);
                    break;
                case DONE:
                    doneTasks.add(task);
                    break;
            }
        }

        return new KanbanBoard(id, boardName, todoTasks, inProgressTasks, doneTasks);
    }

    private String getKanbanBoardName(UUID id) {
        Optional<KanbanBoardDao> kanbanBoardDaoOptional = kanbanRepo.findById(id);
        if (kanbanBoardDaoOptional.isEmpty()) {
            throw new ResourceNotFoundException("Kanbanboard with ID " + id + " does not exist");
        }

        String boardName = kanbanBoardDaoOptional.get().getName();
        return boardName;
    }
}

