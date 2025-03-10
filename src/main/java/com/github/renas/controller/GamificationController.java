package com.github.renas.controller;

import com.github.renas.service.XpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gamification")
class GamificationController {

    private final XpService xpService;

    GamificationController(XpService xpService) {
        this.xpService = xpService;
    }

    @GetMapping("/get-level")
    public int getLevel() {
        return xpService.getUserLevel();
    }

    @GetMapping("/leaderboard")
    public List<Map<String, Object>> getLeaderboard() {
        return xpService.getLeaderboard();
    }
}
