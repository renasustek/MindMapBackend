package com.github.renas.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.renas.requests.KanbanBoard;
import com.github.renas.requests.task.Task;
import com.github.renas.requests.task.TaskStatus;
import com.github.renas.service.KanbanBoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.UUID;

class KanbanBoardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private KanbanBoardService kanbanBoardService;

    @InjectMocks
    private KanbanBoardController kanbanBoardController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(kanbanBoardController).build();
    }

    @Test
    void getKanbanBoard_ReturnsKanbanBoardDetails() throws Exception {
        UUID kanbanBoardId = UUID.randomUUID();
        List<Task> todo = List.of(new Task(UUID.randomUUID(), "Todo Task 1", "Description 1", null, UUID.randomUUID(), new Date(), new Date(), null, TaskStatus.TODO));
        List<Task> inProgress = List.of(new Task(UUID.randomUUID(), "In Progress Task 1", "Description 2", null, UUID.randomUUID(), new Date(), new Date(), null, TaskStatus.INPROGRESS));
        List<Task> done = List.of(new Task(UUID.randomUUID(), "Done Task 1", "Description 3", null, UUID.randomUUID(), new Date(), new Date(), new Date(), TaskStatus.DONE));

        KanbanBoard expectedBoard = new KanbanBoard(kanbanBoardId, "My Kanban Board", todo, inProgress, done);
        given(kanbanBoardService.getKanbanBoard(kanbanBoardId)).willReturn(expectedBoard);

        mockMvc.perform(get("/kanbanBoard/get/" + kanbanBoardId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("My Kanban Board"))
                .andExpect(jsonPath("$.todo[0].name").value("Todo Task 1"))
                .andExpect(jsonPath("$.inprogress[0].name").value("In Progress Task 1"))
                .andExpect(jsonPath("$.done[0].name").value("Done Task 1"));
    }
}
