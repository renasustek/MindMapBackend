package com.github.renas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.renas.exceptions.ResourceNotFoundException;
import com.github.renas.requests.task.EisenhowerMatrix;
import com.github.renas.requests.task.Task;
import com.github.renas.requests.task.TaskRequestForCreate;
import com.github.renas.requests.task.TaskStatus;
import com.github.renas.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import static com.github.renas.requests.task.EisenhowerMatrix.DO;
import static com.github.renas.requests.task.TaskStatus.TODO;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID uuid = UUID.randomUUID();
    private final String name = "Test Task";
    private final String description = "This is a test task";
    private final EisenhowerMatrix eisenhowerMatrix = DO;
    private final String label = "Test Label";
    private final Date date;
    private final TaskStatus taskStatus = TODO;
    private final Task task;
    private final TaskRequestForCreate taskRequestForCreate;

    public TaskControllerTest() {
        Calendar calendar = new GregorianCalendar(2025, Calendar.MARCH, 1);
        this.date = calendar.getTime();
        this.task = new Task(uuid, name, description, eisenhowerMatrix, label, date, taskStatus);
        this.taskRequestForCreate = new TaskRequestForCreate(name, description, eisenhowerMatrix, label, date, taskStatus);
    }

    @DisplayName("GET - When get task given valid id and task is present, should return task")
    @Test
    void whenGetTaskGivenValidIdAndTaskIsPresentShouldReturnTask() throws Exception {
        when(taskService.getById(uuid)).thenReturn(task);

        mockMvc.perform(get("/task/get/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(uuid.toString()))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.eisenhowerMatrix").value(eisenhowerMatrix.toString()))
                .andExpect(jsonPath("$.label").value(label))
                .andExpect(jsonPath("$.date").value(objectMapper.writeValueAsString(date).replace("\"", ""))) // Format date correctly
                .andExpect(jsonPath("$.taskStatus").value(taskStatus.toString()));
    }

    @DisplayName("GET - When get task given valid id but task not found, should return 404 Not Found")
    @Test
    void whenGetTaskGivenInvalidIdShouldReturnNotFound() throws Exception {
        when(taskService.getById(uuid)).thenThrow(new ResourceNotFoundException("Task not found"));

        mockMvc.perform(get("/task/get/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("POST - When a valid task request is sent, should return created task")
    @Test
    void whenAValidTaskRequestShouldReturnTask() throws Exception {
        when(taskService.create(taskRequestForCreate)).thenReturn(task);

        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestForCreate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(uuid.toString()))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.eisenhowerMatrix").value(eisenhowerMatrix.toString()))
                .andExpect(jsonPath("$.label").value(label))
                .andExpect(jsonPath("$.date").value(objectMapper.writeValueAsString(date).replace("\"", "")))
                .andExpect(jsonPath("$.taskStatus").value(taskStatus.toString()));
    }

    @DisplayName("DELETE - When delete with valid id, should return 200 OK")
    @Test
    void whenValidIdGivenShouldReturn200() throws Exception {
        doNothing().when(taskService).delete(uuid);

        mockMvc.perform(delete("/task/delete/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully."));
    }

    @DisplayName("DELETE - If delete is not successful, should return response saying 'Not deleted'")
    @Test
    void comeUpWithANameHere() throws Exception {
        doThrow(new ResourceNotFoundException("Task not found")).when(taskService).delete(uuid);

        mockMvc.perform(delete("/task/delete/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Task not found"));
        ;
    }

    @DisplayName("PUT - When update is called with valid task, should return updated task")
    @Test
    void whenUpdateTaskWithValidDataShouldReturnUpdatedTask() throws Exception {
        when(taskService.update(task)).thenReturn(task);

        mockMvc.perform(put("/task/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(uuid.toString()))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.eisenhowerMatrix").value(eisenhowerMatrix.toString()))
                .andExpect(jsonPath("$.label").value(label))
                .andExpect(jsonPath("$.date").value(objectMapper.writeValueAsString(date).replace("\"", "")))
                .andExpect(jsonPath("$.taskStatus").value(taskStatus.toString()));
    }
}
