package com.github.renas.controller;

import com.github.renas.service.ProcastinationGraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/procrastinationLevel")
public class ProcrastinationController {

    private final ProcastinationGraphService procastinationGraphService;

    public ProcrastinationController(ProcastinationGraphService procastinationGraphService) {
        this.procastinationGraphService = procastinationGraphService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getProcrastinationScores(
            @RequestParam(name = "intervalDays") int intervalDays,
            @RequestParam(name = "timeframeDays") int timeframeDays) {

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(timeframeDays);

        List<Map<String, Object>> responseList = procastinationGraphService.calculateProcrastinationScores(startDate, endDate, intervalDays);

        return ResponseEntity.ok(responseList);
    }
}
