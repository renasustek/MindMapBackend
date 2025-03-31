import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.renas.controller.TaskController;
import com.github.renas.requests.task.Task;
import com.github.renas.requests.task.TaskRequestForCreate;
import com.github.renas.requests.task.TaskStatus;
import com.github.renas.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setup() {
        sampleTask = new Task();
        sampleTask.setId(UUID.randomUUID());
        sampleTask.setName("Sample Task");
        sampleTask.setDescription("Sample Task Description");
        sampleTask.setStatus(TaskStatus.TODO);
    }

    @Test
    void testGetTaskById() throws Exception {
        given(taskService.getById(sampleTask.getId())).willReturn(sampleTask);

        mockMvc.perform(get("/task/get/" + sampleTask.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(sampleTask.getName()));
    }

    @Test
    void testCreateTask() throws Exception {
        TaskRequestForCreate taskRequestForCreate = new TaskRequestForCreate(
                "New Task", "New Description", TaskStatus.TODO);

        given(taskService.create(any(TaskRequestForCreate.class))).willReturn(sampleTask);

        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestForCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sample Task"));
    }

    @Test
    void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/task/delete/" + sampleTask.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully."));
    }

    @Test
    void testUpdateTask() throws Exception {
        given(taskService.update(any(Task.class))).willReturn(sampleTask);

        mockMvc.perform(put("/task/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Sample Task Description"));
    }

    @Test
    void testChangeStatus() throws Exception {
        TaskStatus newStatus = TaskStatus.DONE;
        given(taskService.changeStatus(any(UUID.class), any(TaskStatus.class))).willReturn(newStatus);

        mockMvc.perform(post("/task/change-status/" + sampleTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskStatusRequest(newStatus))))
                .andExpect(status().isOk())
                .andExpect(content().string(newStatus.toString()));
    }
}
