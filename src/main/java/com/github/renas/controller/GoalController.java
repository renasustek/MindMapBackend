package com.github.renas.controller;

import com.github.renas.requests.task.Task;
import com.github.renas.requests.task.TaskRequestForCreate;
import com.github.renas.service.TaskService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://localhost:8080")
@RequestMapping(value = "/goal", produces = MediaType.APPLICATION_JSON_VALUE)
public class GoalController {
    private final GoalService GoalService;

    public GoalController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Task> getTask(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskRequestForCreate task) {
        return ResponseEntity.ok(taskService.create(task));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable UUID id) {
        taskService.delete(id);
        return ResponseEntity.ok("Task deleted successfully.");
    }

    @PutMapping("/update")
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.update(task));
    }
}
