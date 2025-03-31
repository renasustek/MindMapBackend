package com.github.renas.service;

import com.github.renas.exceptions.ResourceNotFoundException;
import com.github.renas.persistance.KanbanRepo;
import com.github.renas.persistance.TaskKanbanRepo;
import com.github.renas.persistance.TaskRepo;
import com.github.renas.persistance.models.KanbanBoardDao;
import com.github.renas.persistance.models.TaskDao;
import com.github.renas.requests.KanbanBoard;
import com.github.renas.requests.task.EisenhowerMatrix;
import com.github.renas.requests.task.Task;
import com.github.renas.requests.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KanbanBoardServiceTest {

    @Mock
    private KanbanRepo kanbanRepo;

    @Mock
    private TaskRepo taskRepo;

    @Mock
    private TaskKanbanRepo taskKanbanRepo;

    @InjectMocks
    private KanbanBoardService kanbanBoardService;

    private UUID boardId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        boardId = UUID.randomUUID();
    }

    @Test
    void getKanbanBoard_returnsBoardWithCategorisedTasks() {
        // Arrange
        KanbanBoardDao boardDao = new KanbanBoardDao();
        boardDao.setUuid(boardId);
        boardDao.setName("My Board");
        when(kanbanRepo.findByIdUserId(boardId)).thenReturn(Optional.of(boardDao));

        UUID taskId = UUID.randomUUID();
        List<UUID> taskIds = List.of(taskId);
        when(taskKanbanRepo.findTaskIdsForCurrentUser(boardId)).thenReturn(taskIds);

        TaskDao taskDao = new TaskDao();
        taskDao.setUuid(taskId);
        taskDao.setName("Task 1");
        taskDao.setDescription("Desc");
        taskDao.setEisenhower(EisenhowerMatrix.DO);
        taskDao.setLabelId(UUID.randomUUID());
        taskDao.setCreatedDate(new Date());
        taskDao.setDueDate(new Date());
        taskDao.setCompletedDate(null);
        taskDao.setTaskStatus(TaskStatus.TODO);

        when(taskRepo.findAllForCurrentUserByTaskIds(taskIds)).thenReturn(List.of(taskDao));

        // Act
        KanbanBoard board = kanbanBoardService.getKanbanBoard(boardId);

        // Assert
        assertEquals("My Board", board.name());
        assertEquals(1, board.todo().size());
        assertEquals(0, board.inprogress().size());
        assertEquals(0, board.done().size());
    }

    @Test
    void getKanbanBoard_throwsExceptionWhenBoardNotFound() {
        // Arrange
        when(kanbanRepo.findByIdUserId(boardId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> kanbanBoardService.getKanbanBoard(boardId));
    }
}
