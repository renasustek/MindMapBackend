package com.github.renas.controller;

import com.github.renas.requests.LabelRequest;
import com.github.renas.requests.task.Label;
import com.github.renas.service.LabelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    // CREATE - Add a new label
    @PostMapping
    public ResponseEntity<Label> createLabel(@RequestBody LabelRequest labelRequest) {
        Label createdLabel = labelService.createLabel(labelRequest);
        return ResponseEntity.ok(createdLabel);
    }
    @GetMapping
    public ResponseEntity<List<Label>> getAllLabels() {
        List<Label> labels = labelService.getAllLabels();
        return ResponseEntity.ok(labels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Label> getLabelById(@PathVariable UUID id) {
        Label label = labelService.getLabelById(id);
        return ResponseEntity.ok(label);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Label> updateLabel(@PathVariable UUID id, @RequestBody Label label) {
        Label updatedLabel = labelService.updateLabel(id, label);
        return ResponseEntity.ok(updatedLabel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable UUID id) {
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }
}
