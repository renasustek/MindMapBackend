package com.github.renas.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.renas.requests.LabelRequest;
import com.github.renas.requests.task.Label;
import com.github.renas.service.LabelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = LabelController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LabelService labelService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createLabel_returnsLabel() throws Exception {
        LabelRequest request = new LabelRequest("Urgent");
        Label response = new Label(UUID.randomUUID(), "Urgent");
        given(labelService.createLabel(any(LabelRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Urgent"));
    }

    @Test
    void getAllLabels_returnsListOfLabels() throws Exception {
        List<Label> labels = List.of(new Label(UUID.randomUUID(), "Urgent"), new Label(UUID.randomUUID(), "High Priority"));
        given(labelService.getAllLabels()).willReturn(labels);

        mockMvc.perform(get("/api/labels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Urgent"))
                .andExpect(jsonPath("$[1].name").value("High Priority"));
    }

    @Test
    void getLabelById_returnsLabel() throws Exception {
        UUID id = UUID.randomUUID();
        Label label = new Label(id, "Urgent");
        given(labelService.getLabelById(id)).willReturn(label);

        mockMvc.perform(get("/api/labels/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Urgent"));
    }

    @Test
    void updateLabel_returnsUpdatedLabel() throws Exception {
        UUID id = UUID.randomUUID();
        Label updateRequest = new Label(id, "Not Urgent");
        given(labelService.updateLabel(eq(id), any(Label.class))).willReturn(updateRequest);

        mockMvc.perform(put("/api/labels/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Not Urgent"));
    }

    @Test
    void deleteLabel_performsDeletion() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(labelService).deleteLabel(id);

        mockMvc.perform(delete("/api/labels/" + id))
                .andExpect(status().isNoContent());
    }
}
