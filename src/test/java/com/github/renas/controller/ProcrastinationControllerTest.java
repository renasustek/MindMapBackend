package com.github.renas.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.renas.service.ProcastinationGraphService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebMvcTest(controllers = ProcrastinationController.class, excludeAutoConfiguration = SecurityAutoConfiguration .class)
public class ProcrastinationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcastinationGraphService procrastinationGraphService;

    @Test
    void getProcrastinationScores_returnsScores() throws Exception {
        int intervalDays = 7;
        int timeframeDays = 30;

        List<Map<String, Object>> scores = new ArrayList<>();
        Map<String, Object> score = new HashMap<>();
        score.put("startDate", LocalDate.now().minusDays(30));
        score.put("endDate", LocalDate.now());
        score.put("procrastinationScore", 50.0);
        scores.add(score);

        given(procrastinationGraphService.calculateProcrastinationScores(any(LocalDate.class), any(LocalDate.class), eq(intervalDays)))
                .willReturn(scores);

        mockMvc.perform(get("/procrastinationLevel")
                        .param("intervalDays", String.valueOf(intervalDays))
                        .param("timeframeDays", String.valueOf(timeframeDays)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].procrastinationScore").value(50.0));
    }
}
