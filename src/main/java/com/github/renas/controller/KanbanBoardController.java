package com.github.renas.controller;
import com.github.renas.persistance.models.KanbanBoardDao;
import com.github.renas.requests.KanbanBoard;
import com.github.renas.service.KanbanBoardService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://localhost:8080")
@RequestMapping(value = "/kanbanBoard", produces = MediaType.APPLICATION_JSON_VALUE)
public class KanbanBoardController {
    private final KanbanBoardService kanbanBoardService;

    public KanbanBoardController(KanbanBoardService kanbanBoardService) {
        this.kanbanBoardService = kanbanBoardService;
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<KanbanBoard> getKanbanBoard(@PathVariable UUID id) {
        return ResponseEntity.ok(kanbanBoardService.getKanbanBoard(id));
    }
}
