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
public class ProcastinationGraphService {

    private final TaskRepo taskRepo;

    private final ChatbotRepo chatbotRepo;

    public ProcastinationGraphService(TaskRepo taskRepo, ChatbotRepo chatbotRepo) {
        this.taskRepo = taskRepo;
        this.chatbotRepo = chatbotRepo;

    }

    public List<Map<String, Object>> calculateProcrastinationScores(LocalDate startDate, LocalDate endDate, int intervalDays) {
        List<Map<String, Object>> results = new ArrayList<>();


        LocalDate currentStart = startDate;
        while (currentStart.isBefore(endDate)) {
            LocalDate currentEnd = currentStart.plusDays(intervalDays).minusDays(1);
            if (currentEnd.isAfter(endDate)) {
                currentEnd = endDate;
            }


            double score = calculateProcrastinationScore(currentStart, currentEnd);


            Map<String, Object> result = new HashMap<>();
            result.put("startDate", currentStart);
            result.put("endDate", currentEnd);
            result.put("procrastinationScore", score);
            results.add(result);


            currentStart = currentEnd.plusDays(1);
        }
        return results;
    }

    private double calculateProcrastinationScore(LocalDate startDate, LocalDate endDate) {
        long completedOnTime = taskRepo.countTasksCompletedOnTimeForCurrentUser(startDate, endDate);
        long totalTasks = taskRepo.countTotalTasksForCurrentUser(startDate, endDate);


        if (totalTasks == 0) {
            return 0.0;
        }

        double TCR = (double) completedOnTime / totalTasks;

        Double avgSentimentScore = chatbotRepo.findAverageSentimentScoreForCurrentUser(startDate, endDate);


        if (avgSentimentScore == null) {
            avgSentimentScore = 0.5;
        }

        return (1 - TCR) * 100 + (1 - avgSentimentScore) * 50;
    }
}

