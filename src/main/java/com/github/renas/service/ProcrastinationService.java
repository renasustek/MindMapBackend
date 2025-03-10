package com.github.renas.service;

import com.github.renas.persistance.ChatbotRepo;
import com.github.renas.persistance.TaskRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcrastinationService {

    private final TaskRepo taskRepo;

    private final ChatbotRepo chatbotRepo;

    public ProcrastinationService(TaskRepo taskRepo, ChatbotRepo chatbotRepo) {
        this.taskRepo = taskRepo;
        this.chatbotRepo = chatbotRepo;

    }

    public List<Map<String, Object>> calculateProcrastinationScores(LocalDate startDate, LocalDate endDate, int intervalDays) {
        List<Map<String, Object>> results = new ArrayList<>();

        // Iterate through the timeframe in intervals
        LocalDate currentStart = startDate;
        while (currentStart.isBefore(endDate)) {
            LocalDate currentEnd = currentStart.plusDays(intervalDays).minusDays(1);
            if (currentEnd.isAfter(endDate)) {
                currentEnd = endDate;
            }

            // Compute procrastination score for the interval
            double score = calculateProcrastinationScore(currentStart, currentEnd);

            // Store results
            Map<String, Object> result = new HashMap<>();
            result.put("startDate", currentStart);
            result.put("endDate", currentEnd);
            result.put("procrastinationScore", score);
            results.add(result);

            // Move to next interval
            currentStart = currentEnd.plusDays(1);
        }
        return results;
    }

    private double calculateProcrastinationScore(LocalDate startDate, LocalDate endDate) {
        long completedOnTime = taskRepo.countTasksCompletedOnTimeForCurrentUser(startDate, endDate);
        long totalTasks = taskRepo.countTotalTasksForCurrentUser(startDate, endDate);

        // If no tasks exist in this interval, return 0
        if (totalTasks == 0) {
            return 0.0;
        }

        double TCR = (double) completedOnTime / totalTasks;

        Double avgSentimentScore = chatbotRepo.findAverageSentimentScoreForCurrentUser(startDate, endDate);

        // If no sentiment data exists, assume neutral (0.5) OR return 0.
        if (avgSentimentScore == null) {
            avgSentimentScore = 0.5;
        }

        return (1 - TCR) * 100 + (1 - avgSentimentScore) * 50;
    }
}

