package com.github.renas.service;

import com.github.renas.persistance.TaskRepo;
import com.github.renas.requests.task.*;
import com.github.renas.persistance.models.TaskDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainKanbanServiceTest {

    @Mock
    private TaskRepo taskRepo;

    @Mock
    private TaskPrioritisation taskPrioritisation;

    @InjectMocks
    private MainKanbanService mainKanbanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTaskPrioritisation_ReturnsSortedTop10Tasks() {
        TaskDao taskDao1 = new TaskDao();
        taskDao1.setUuid(UUID.randomUUID());
        taskDao1.setName("Task 1");
        taskDao1.setDescription("Description 1");
        taskDao1.setEisenhower(EisenhowerMatrix.DO);
        taskDao1.setLabelId(UUID.randomUUID());
        taskDao1.setCreatedDate(new Date());
        taskDao1.setDueDate(new Date());
        taskDao1.setCompletedDate(null);
        taskDao1.setTaskStatus(TaskStatus.TODO);

        TaskDao taskDao2 = new TaskDao();
        taskDao2.setUuid(UUID.randomUUID());
        taskDao2.setName("Task 2");
        taskDao2.setDescription("Description 2");
        taskDao2.setEisenhower(EisenhowerMatrix.DECIDE);
        taskDao2.setLabelId(UUID.randomUUID());
        taskDao2.setCreatedDate(new Date());
        taskDao2.setDueDate(new Date());
        taskDao2.setCompletedDate(null);
        taskDao2.setTaskStatus(TaskStatus.TODO);

        List<TaskDao> taskDaos = Arrays.asList(taskDao1, taskDao2);

        when(taskRepo.findAllTasksByUserId()).thenReturn(taskDaos);
        when(taskPrioritisation.calculatePriorityScore(any(Task.class))).thenReturn(1.0);

        List<Task> result = mainKanbanService.getTaskPrioritisation();

        assertEquals(2, result.size());
        verify(taskRepo, times(1)).findAllTasksByUserId();
        verify(taskPrioritisation, times(2)).calculatePriorityScore(any(Task.class));
    }
}
