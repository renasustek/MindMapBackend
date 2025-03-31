package com.github.renas.service;

import com.github.renas.exceptions.ResourceNotFoundException;
import com.github.renas.gamification.Rewards;
import com.github.renas.persistance.TaskKanbanRepo;
import com.github.renas.persistance.TaskRepo;
import com.github.renas.persistance.models.TaskDao;
import com.github.renas.persistance.models.TaskKanbanDao;
import com.github.renas.persistance.models.UserDao;
import com.github.renas.requests.task.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepo taskRepo;

    @Mock
    private TaskKanbanRepo taskKanbanRepo;

    @Mock
    private XpService xpService;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepo, null, taskKanbanRepo, xpService);

        // Correctly mocking UserDao as principal
        UserDao mockUser = new UserDao();
        UUID fakeUserId = UUID.randomUUID();
        mockUser.setId(fakeUserId);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser); // ✅ Pass UserDao

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getById_shouldReturnTask_whenFound() {
        UUID id = UUID.randomUUID();
        TaskDao dao = new TaskDao();
        dao.setUuid(id);
        dao.setName("Test Task");
        dao.setEisenhower(EisenhowerMatrix.DO);
        dao.setLabelId(UUID.randomUUID());
        dao.setCreatedDate(Date.valueOf(LocalDate.now()));
        dao.setDueDate(Date.valueOf(LocalDate.now().plusDays(1)));
        dao.setTaskStatus(TaskStatus.TODO);

        when(taskRepo.findTaskByIdForCurrentUser(id)).thenReturn(Optional.of(dao));

        Task task = taskService.getById(id);

        assertEquals(id, task.id());
    }

    @Test
    void create_shouldReturnCreatedTask() {
        TaskRequestForCreate request = new TaskRequestForCreate(UUID.randomUUID(), "Title", "Desc", EisenhowerMatrix.DO, UUID.randomUUID(), new java.util.Date());

        ArgumentCaptor<TaskDao> taskCaptor = ArgumentCaptor.forClass(TaskDao.class);
        when(taskRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Task created = taskService.create(request);

        assertEquals("Title", created.name());
        verify(taskRepo).save(taskCaptor.capture());
        assertEquals(TaskStatus.TODO, taskCaptor.getValue().getTaskStatus());
    }

    @Test
    void delete_shouldCallDelete_whenExists() {
        UUID id = UUID.randomUUID();
        when(taskRepo.existsById(id)).thenReturn(true);

        taskService.delete(id);

        verify(taskRepo).deleteById(id);
    }

    @Test
    void delete_shouldThrowException_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(taskRepo.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taskService.delete(id));
    }

    @Test
    void update_shouldReturnUpdatedTask() {
        UUID id = UUID.randomUUID();
        Task task = new Task(id, "Updated", "Desc", EisenhowerMatrix.DO, UUID.randomUUID(), new java.util.Date(), new java.util.Date(), null, TaskStatus.INPROGRESS);

        when(taskRepo.existsById(id)).thenReturn(true);
        when(taskRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Task updated = taskService.update(task);

        assertEquals("Updated", updated.name());
    }

    @Test
    void changeStatus_shouldUpdateStatusToDone_andAddXP() {
        UUID id = UUID.randomUUID();
        TaskDao dao = new TaskDao();
        dao.setUuid(id);
        dao.setTaskStatus(TaskStatus.INPROGRESS);
        dao.setDueDate(Date.valueOf(LocalDate.now()));

        when(taskRepo.findTaskByIdForCurrentUser(id)).thenReturn(Optional.of(dao));
        when(taskRepo.save(any())).thenReturn(dao);

        TaskStatus newStatus = taskService.changeStatus(id, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, newStatus);
        verify(xpService).addXP(Rewards.TASK_COMPLETED);
    }

    @Test
    void changeStatus_shouldUpdateStatusToInProgress_andRemoveCompletedDate() {
        UUID id = UUID.randomUUID();
        TaskDao dao = new TaskDao();
        dao.setUuid(id);
        dao.setTaskStatus(TaskStatus.DONE);
        dao.setCompletedDate(Date.valueOf(LocalDate.now()));

        when(taskRepo.findTaskByIdForCurrentUser(id)).thenReturn(Optional.of(dao));
        when(taskRepo.save(any())).thenReturn(dao);

        TaskStatus newStatus = taskService.changeStatus(id, TaskStatus.INPROGRESS);

        assertEquals(TaskStatus.INPROGRESS, newStatus);
        assertNull(dao.getCompletedDate());
    }
}