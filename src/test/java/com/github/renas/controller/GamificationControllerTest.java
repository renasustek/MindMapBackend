package com.github.renas.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.renas.service.XpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebMvcTest(controllers = GamificationController.class, excludeAutoConfiguration = SecurityAutoConfiguration .class)
public class GamificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private XpService xpService;

    @Test
    void getLevel_ReturnsLevel() throws Exception {
        given(xpService.getUserLevel()).willReturn(5);

        mockMvc.perform(get("/gamification/get-level"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void getLeaderboard_ReturnsLeaderboard() throws Exception {
        List<Map<String, Object>> leaderboard = new ArrayList<>();
        Map<String, Object> entry = new HashMap<>();
        entry.put("username", "user1");
        entry.put("xp", 1500);
        entry.put("level", 12);
        leaderboard.add(entry);

        given(xpService.getLeaderboard()).willReturn(leaderboard);

        mockMvc.perform(get("/gamification/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'username':'user1','xp':1500,'level':12}]"));
    }
}
