package com.github.renas.service;

import com.github.renas.persistance.ChatbotRepo;
import com.github.renas.persistance.LabelChatbotMessageRepo;
import com.github.renas.persistance.TaskRepo;
import com.github.renas.requests.task.EisenhowerMatrix;
import com.github.renas.requests.task.Task;
import com.github.renas.requests.task.TaskStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class TaskPrioritisation {
    private final TaskRepo taskRepo;
    private final LabelService labelService;
    private final LabelChatbotMessageRepo labelChatbotRepo;
    private final ChatbotRepo chatbotRepo;


    public TaskPrioritisation(TaskRepo taskRepo, LabelService labelService, LabelChatbotMessageRepo labelChatbotRepo, ChatbotRepo chatbotRepo) {
        this.taskRepo = taskRepo;
        this.labelService = labelService;
        this.labelChatbotRepo = labelChatbotRepo;
        this.chatbotRepo = chatbotRepo;
    }

    public double calculatePriorityScore(Task task){
        if (task.taskStatus()== TaskStatus.DONE) return 0;

        return (0.3 * timeFactor(task.createdDate(), task.dueDate())) + (0.3 * overdueFactor(task.dueDate())) +
                (0.2 * eisenhowerFactor(task.eisenhowerMatrix())) + (0.2 * sentimentFactor(task.labelId()));
    }

    private double eisenhowerFactor(EisenhowerMatrix category) {
        return switch (category.name().toLowerCase()) {
            case "do" -> 1.5;
            case "delegate" -> 1.2;
            case "schedule" -> 1.0;
            case "delete" -> 0.5;
            default -> 1.0;
        };
    }
    private double overdueFactor(Date dueDate){

        LocalDate dueLocalDate = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long daysToDueDate = ChronoUnit.DAYS.between(LocalDate.now(), dueLocalDate);

        return daysToDueDate < 0 ? (1 + Math.abs(daysToDueDate)) : 0;
    }

    private double timeFactor(Date createdDate, Date dueDate){

        LocalDate createdLocalDate = createdDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dueLocalDate = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long daysSinceCreated = ChronoUnit.DAYS.between(createdLocalDate, LocalDate.now());
        long daysToDueDate = ChronoUnit.DAYS.between(LocalDate.now(), dueLocalDate);

        return (double) daysSinceCreated / (daysToDueDate + 1);

    }

    private double sentimentFactor(UUID labelId){
        List<Float> sentimentScoreList = new ArrayList<>();
        labelChatbotRepo.findChatbotIdUsingLabelId(labelId).stream().map(chatbotId ->
                sentimentScoreList.add(chatbotRepo.findSentimentScore(chatbotId))
                );

        if (sentimentScoreList.isEmpty()) return 1.0;

        double avgSentiment = sentimentScoreList.stream().mapToDouble(Float::doubleValue).average().orElse(0.0);
        return 1 - avgSentiment;
    }
}
