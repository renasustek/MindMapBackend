package com.github.renas.controller;

import com.github.renas.requests.task.Task;
import com.github.renas.service.MainKanbanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mainKanban")
public class MainKanbanController {
    private final MainKanbanService mainKanbanService;

    public MainKanbanController(MainKanbanService mainKanbanService) {
        this.mainKanbanService = mainKanbanService;
    }

    @GetMapping()
    public ResponseEntity<List<Task>> getPrioritisedTasks() {
        List<Task> prioritisedTasks = mainKanbanService.getTaskPrioritisation();
        return ResponseEntity.ok(prioritisedTasks);
    }
}
