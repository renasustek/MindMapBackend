package com.github.renas.service;

import com.github.renas.exceptions.ResourceNotFoundException;
import com.github.renas.persistance.TaskRepo;
import com.github.renas.persistance.models.TaskDao;
import com.github.renas.requests.task.EisenhowerMatrix;
import com.github.renas.requests.task.Task;
import com.github.renas.requests.task.TaskRequestForCreate;
import com.github.renas.requests.task.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepo taskRepo;

    @InjectMocks
    private TaskService taskService;

    private final UUID uuid = UUID.randomUUID();
    private final String name = "name";
    private final String description = "name";
    private final EisenhowerMatrix eisenhowerMatrix = EisenhowerMatrix.DO; // assuming you meant this here
    private final String label = "name";
    private final Date date;
    private final TaskStatus taskStatus = TaskStatus.TODO;
    private final Task task;
    private final TaskRequestForCreate taskRequestForCreate;
    private final UUID taskId = UUID.randomUUID();
    private final TaskDao taskDao = createTaskDao();
    private final TaskRequestForCreate taskRequest = createTaskRequest();

    public TaskServiceTest() {
        Calendar calendar = new GregorianCalendar(2025, Calendar.MARCH, 1);
        this.date = calendar.getTime();
        this.task = new Task(uuid, name, description, eisenhowerMatrix, label, calendar.getTime(), taskStatus);
        this.taskRequestForCreate = new TaskRequestForCreate(name, description, eisenhowerMatrix, label, calendar.getTime(), taskStatus);
    }

    private TaskDao createTaskDao() {
        TaskDao taskDao = new TaskDao();
        taskDao.setId(taskId);
        taskDao.setName(name);
        taskDao.setDescription(description);
        taskDao.setEisenhower(eisenhowerMatrix);
        taskDao.setLabel(label);
        taskDao.setDueDate(date);
        taskDao.setTaskStatus(taskStatus);
        return taskDao;
    }

    private Task createTask() {
        return new Task(uuid, name, description, eisenhowerMatrix, label, date, taskStatus);
    }

    private TaskRequestForCreate createTaskRequest() {
        return new TaskRequestForCreate(name, description, eisenhowerMatrix, label, date, taskStatus);
    }

    @DisplayName("GET - Given valid task ID, should return Task")
    @Test
    void givenValidTaskId_WhenGetById_ShouldReturnTask() {
        given(taskRepo.findById(taskId)).willReturn(Optional.of(taskDao));

        Task foundTask = taskService.getById(taskId);

        assertThat(foundTask).isNotNull();
        assertThat(foundTask.id()).isEqualTo(taskId);
        then(taskRepo).should(times(1)).findById(taskId);
    }

    @DisplayName("GET - Given invalid task ID, should throw ResourceNotFoundException")
    @Test
    void givenInvalidTaskId_WhenGetById_ShouldThrowException() {
        given(taskRepo.findById(taskId)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getById(taskId));
        then(taskRepo).should(times(1)).findById(taskId);
    }


    @DisplayName("POST - Given valid task request, should create and return Task")
    @Test
    void givenValidTaskRequest_WhenCreate_ShouldReturnTask() {
        given(taskRepo.save(any(TaskDao.class))).willReturn(taskDao);

        Task createdTask = taskService.create(taskRequest);

        assertThat(createdTask).isNotNull();
        assertThat(createdTask.id()).isEqualTo(taskId);
        assertThat(createdTask.name()).isEqualTo(taskDao.getName());
        then(taskRepo).should(times(1)).save(any(TaskDao.class));
    }

    @DisplayName("POST - Given null task request, should throw NullPointerException")
    @Test
    void givenNullTaskRequest_WhenCreate_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> taskService.create(null));
    }

    @DisplayName("DELETE - Given valid task ID, should delete Task")
    @Test
    void givenValidTaskId_WhenDelete_ShouldDeleteTask() {
        given(taskRepo.existsById(taskId)).willReturn(true);

        taskService.delete(taskId);

        then(taskRepo).should(times(1)).deleteById(taskId);
    }

    @DisplayName("DELETE - Given invalid task ID, should throw ResourceNotFoundException")
    @Test
    void givenInvalidTaskId_WhenDelete_ShouldThrowException() {
        given(taskRepo.existsById(taskId)).willReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taskService.delete(taskId));
        then(taskRepo).should(times(1)).existsById(taskId);
    }

    @DisplayName("PUT - Given valid task, should update and return Task")
    @Test
    void givenValidTask_WhenUpdate_ShouldUpdateAndReturnTask() {
        given(taskRepo.existsById(task.id())).willReturn(true);
        given(taskRepo.save(taskDao)).willReturn(taskDao);

        Task updatedTask = taskService.update(task);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.id()).isEqualTo(taskId);

        then(taskRepo).should(times(1)).deleteById(taskId);
        then(taskRepo).should(times(1)).save(any(TaskDao.class));
    }

    @DisplayName("PUT - Given invalid task ID, should throw ResourceNotFoundException")
    @Test
    void givenInvalidTaskId_WhenUpdate_ShouldThrowException() {
        given(taskRepo.existsById(taskId)).willReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taskService.update(task));
        then(taskRepo).should(times(1)).existsById(taskId);
    }
}
