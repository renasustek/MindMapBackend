package com.github.renas.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.renas.requests.task.Task;
import com.github.renas.service.MainKanbanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = MainKanbanController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class MainKanbanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MainKanbanService mainKanbanService;

    @Test
    void getPrioritisedTasks_returnsTasks() throws Exception {
        List<Task> tasks = List.of(new Task(
                UUID.randomUUID(), "Task 1", "Description 1", null, UUID.randomUUID(), new Date(), new Date(), null, null
        ));
        given(mainKanbanService.getTaskPrioritisation()).willReturn(tasks);

        mockMvc.perform(get("/mainKanban"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Task 1"));
    }
}
