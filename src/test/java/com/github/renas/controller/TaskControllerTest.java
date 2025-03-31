package com.github.renas.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.renas.requests.TaskStatusRequest;
import com.github.renas.requests.task.Task;
import com.github.renas.requests.task.TaskRequestForCreate;
import com.github.renas.requests.task.TaskStatus;
import com.github.renas.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import com.github.renas.requests.task.EisenhowerMatrix;

@WebMvcTest(controllers = TaskController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper; // For converting objects to JSON

    @Test
    void getTask_returnsTask() throws Exception {
        UUID taskId = UUID.randomUUID();
        Task task = new Task(taskId, "Test Task", "Description", EisenhowerMatrix.DO, UUID.randomUUID(), new Date(), new Date(), null, TaskStatus.TODO);
        given(taskService.getById(any(UUID.class))).willReturn(task);

        mockMvc.perform(get("/task/get/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Task"));
    }

    @Test
    void createTask_createsAndReturnsTask() throws Exception {
        TaskRequestForCreate request = new TaskRequestForCreate(UUID.randomUUID(), "New Task", "New Description", EisenhowerMatrix.DO, UUID.randomUUID(), new Date());
        Task expectedTask = new Task(UUID.randomUUID(), "New Task", "New Description", EisenhowerMatrix.DO, UUID.randomUUID(), new Date(), new Date(), null, TaskStatus.TODO);
        given(taskService.create(request)).willReturn(expectedTask);

        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Task"));
    }

    @Test
    void deleteTask_deletesTaskSuccessfully() throws Exception {
        UUID taskId = UUID.randomUUID();
        doNothing().when(taskService).delete(taskId);

        mockMvc.perform(delete("/task/delete/" + taskId))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully."));
    }

    @Test
    void updateTask_updatesAndReturnsTask() throws Exception {
        Task originalTask = new Task(UUID.randomUUID(), "Old Task", "Old Description", EisenhowerMatrix.DO, UUID.randomUUID(), new Date(), new Date(), null, TaskStatus.TODO);
        Task updatedTask = new Task(originalTask.id(), "Updated Task", "Updated Description", originalTask.eisenhowerMatrix(), originalTask.labelId(), originalTask.createdDate(), originalTask.dueDate(), originalTask.completedDate(), TaskStatus.INPROGRESS);
        given(taskService.update(any(Task.class))).willReturn(updatedTask);

        mockMvc.perform(put("/task/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(originalTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Task"));
    }

    @Test
    void changeStatus_changesAndReturnsNewStatus() throws Exception {
        UUID taskId = UUID.randomUUID();
        TaskStatusRequest statusRequest = new TaskStatusRequest(TaskStatus.DONE);
        given(taskService.changeStatus(taskId, TaskStatus.DONE)).willReturn(TaskStatus.DONE);

        mockMvc.perform(post("/task/change-status/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("DONE"));
    }
}
