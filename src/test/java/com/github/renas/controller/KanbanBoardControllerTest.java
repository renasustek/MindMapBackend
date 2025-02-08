package com.github.renas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.renas.exceptions.ResourceNotFoundException;
import com.github.renas.requests.KanbanBoard;
import com.github.renas.requests.task.Task;
import com.github.renas.service.KanbanBoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = KanbanBoardController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class KanbanBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KanbanBoardService kanbanBoardService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID uuid = UUID.randomUUID();
    private final String name = "Test Kanban Board";
    private final List<Task> todoTasks = List.of(new Task(UUID.randomUUID(), "Task 1", "Description", null, "Label", null, null));
    private final List<Task> inProgressTasks = List.of();
    private final List<Task> doneTasks = List.of();
    private final KanbanBoard kanbanBoard = new KanbanBoard(uuid, name, todoTasks, inProgressTasks, doneTasks);

    @DisplayName("GET - When get kanban board given valid id, should return kanban board")
    @Test
    void whenGetKanbanBoardGivenValidIdShouldReturnKanbanBoard() throws Exception {
        when(kanbanBoardService.getKanbanBoard(uuid)).thenReturn(kanbanBoard);

        mockMvc.perform(get("/kanbanBoard/get/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(uuid.toString()))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.todo[0].id").value(todoTasks.get(0).id().toString()))
                .andExpect(jsonPath("$.todo[0].name").value(todoTasks.get(0).name()));
    }

    @DisplayName("GET - When get kanban board given invalid id, should return 404 Not Found")
    @Test
    void whenGetKanbanBoardGivenInvalidIdShouldReturnNotFound() throws Exception {
        when(kanbanBoardService.getKanbanBoard(uuid)).thenThrow(new ResourceNotFoundException("Kanban board not found"));

        mockMvc.perform(get("/kanbanBoard/get/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Kanban board not found"));
    }
}
