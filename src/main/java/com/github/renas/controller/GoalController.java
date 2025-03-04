package com.github.renas.controller;

import com.github.renas.requests.GoalRequest;
import com.github.renas.responses.GoalResponse;
import com.github.renas.service.GoalService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://localhost:8080")
@RequestMapping(value = "/goal", produces = MediaType.APPLICATION_JSON_VALUE)
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping("/create")
    public ResponseEntity<GoalResponse> createGoal(@RequestBody GoalRequest request) {
        return ResponseEntity.ok(goalService.createGoal(request));
    }

    @PutMapping("/complete/{goalId}")
    public ResponseEntity<GoalResponse> completeGoal(@PathVariable UUID goalId) {
        return ResponseEntity.ok(goalService.completeGoal(goalId));
    }

    // Get all Goals
    @GetMapping("/all")
    public ResponseEntity<List<GoalResponse>> getAllGoals() {
        return ResponseEntity.ok(goalService.getAllGoals());
    }
}
