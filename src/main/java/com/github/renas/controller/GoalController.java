package com.github.renas.controller;

import com.github.renas.service.GoalService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://localhost:8080")
@RequestMapping(value = "/goal", produces = MediaType.APPLICATION_JSON_VALUE)
public class GoalController {
    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

}
