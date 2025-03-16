package com.github.renas.triggers;

import com.github.renas.persistance.*;
import com.github.renas.persistance.models.UserDao;
import com.github.renas.requests.task.Task;
import com.github.renas.requests.task.TaskStatus;
import com.github.renas.service.MainKanbanService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * An all-in-one service that calculates motivation & ability,
 * decides which trigger (Spark, Facilitator, or Signal) to fire,
 * crafts a dynamic message, and sends an email every day.
 */
@Service
@Transactional
public class TriggerService {

    private final TaskRepo taskRepo;
    private final ChatbotRepo chatbotRepo;
    private final GoalRepo goalRepo;
    private final UserRepository userRepo;
    private final JavaMailSender mailSender;
    private final MainKanbanService mainKanbanService;

    public TriggerService(TaskRepo taskRepo,
                          ChatbotRepo chatbotRepo,
                          GoalRepo goalRepo,
                          UserRepository userRepo,
                          JavaMailSender mailSender,
                          MainKanbanService mainKanbanService) {
        this.taskRepo = taskRepo;
        this.chatbotRepo = chatbotRepo;
        this.goalRepo = goalRepo;
        this.userRepo = userRepo;
        this.mailSender = mailSender;
        this.mainKanbanService = mainKanbanService;
    }

    /**
     * This method runs once every 24 hours (86,400,000 ms).
     * You can change this to a cron expression if you prefer a specific time of day.
     */
    @Scheduled(fixedRate = 86400000) // 24 hours in milliseconds
    public void scheduledEmailSender() {
        List<UserDao> users = userRepo.findAll();
        for (UserDao user : users) {
            processTrigger(user);
        }
    }

    /**
     * Entry point: Evaluate the user’s data and send an appropriate Fogg trigger email.
     */
    public void processTrigger(UserDao user) {
        double motivation = calculateMotivationValue(user);
        double ability = calculateAbilityValue(user);

        TriggerType triggerType = decideTrigger(motivation, ability);

        String subject = buildSubjectLine(triggerType);
        String body = buildEmailBody(triggerType, user, motivation, ability);

        sendEmail(user.getEmail(), subject, body);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  STEP 1: Calculate MOTIVATION & ABILITY
    // ─────────────────────────────────────────────────────────────────────────

    private double calculateMotivationValue(UserDao user) {
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysAgo = now.minusDays(30);

        // A) Get tasks done in the last 30 days
        long tasksDoneCount = taskRepo.countTasksCompletedOnTime(thirtyDaysAgo, now, user.getId());
        long tasksCreatedCount = taskRepo.countTotalTasks(thirtyDaysAgo, now, user.getId());

        double completionRate = 0.0;
        if (tasksCreatedCount > 0) {
            completionRate = (double) tasksDoneCount / tasksCreatedCount; // range [0..1]
        }

        // B) Use average sentiment from the chatbot to gauge positivity
        Double avgSentiment = chatbotRepo.findAverageSentimentScore(thirtyDaysAgo, now, user.getId());
        if (avgSentiment == null) {
            avgSentiment = 0.5; // if no data, assume neutral
        }

        // Weighted approach: M = 0.6*(completionRate) + 0.4*(avgSentiment)
        double motivation = 0.6 * completionRate + 0.4 * avgSentiment;

        // Return scaled 0..100
        return motivation * 100.0;
    }

    private double calculateAbilityValue(UserDao user) {
        LocalDate now = LocalDate.now();
        long overdueTasks = taskRepo.countOverdueTasks(user.getId(), now);
        long inProgressCount = taskRepo.countTasksByStatus(user.getId(), TaskStatus.INPROGRESS);
        long realisticGoalsCount = goalRepo.countRealisticGoals(user.getId());

        // Start at 100, subtract penalty, add bonus
        double baseAbility = 100.0;

        double overduePenalty = Math.min(overdueTasks * 2.5, 50);
        baseAbility -= overduePenalty;

        double inProgressPenalty = Math.min(inProgressCount * 1.5, 30);
        baseAbility -= inProgressPenalty;

        baseAbility += realisticGoalsCount * 5; // each realistic goal adds 5

        // clamp to [0..100]
        if (baseAbility < 0) baseAbility = 0;
        if (baseAbility > 100) baseAbility = 100;

        return baseAbility;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  STEP 2: Decide Which Trigger
    // ─────────────────────────────────────────────────────────────────────────

    private TriggerType decideTrigger(double motivation, double ability) {
        double threshold = 50.0;

        boolean isMotivationLow = (motivation < threshold);
        boolean isAbilityLow = (ability < threshold);

        if (isMotivationLow && !isAbilityLow) {
            // Spark
            return TriggerType.SPARK;
        } else if (!isMotivationLow && isAbilityLow) {
            // Facilitator
            return TriggerType.FACILITATOR;
        } else {
            // Signal
            return TriggerType.SIGNAL;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  STEP 3: Craft the Email
    // ─────────────────────────────────────────────────────────────────────────

    private String buildSubjectLine(TriggerType triggerType) {
        switch (triggerType) {
            case SPARK:
                return "We believe in you! (Spark)";
            case FACILITATOR:
                return "Let’s simplify your tasks (Facilitator)";
            case SIGNAL:
            default:
                return "Daily Check-In (Signal)";
        }
    }

    private String buildEmailBody(TriggerType triggerType, UserDao user, double motivation, double ability) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hi ").append(user.getUsername()).append(",\n\n");

        switch (triggerType) {
            case SPARK:
                sb.append("Your MOTIVATION is on the lower side (")
                        .append(String.format("%.1f", motivation)).append("),\n")
                        .append("but your ABILITY is relatively high (")
                        .append(String.format("%.1f", ability)).append(")!\n")
                        .append("This means you can definitely do it! Focus on completing just one key task to build momentum.\n")
                        .append("\nHere's a quick overview:\n");
                appendSparkData(sb, user);
                break;

            case FACILITATOR:
                sb.append("You have strong MOTIVATION (")
                        .append(String.format("%.1f", motivation)).append("),\n")
                        .append("but your ABILITY is struggling (")
                        .append(String.format("%.1f", ability)).append(").\n")
                        .append("Let's simplify your tasks. Consider delegating or dividing big tasks into smaller steps.\n")
                        .append("\nTry focusing on these high-priority tasks:\n");
                appendFacilitatorData(sb, user);
                break;

            case SIGNAL:
                sb.append("You have decent MOTIVATION (")
                        .append(String.format("%.1f", motivation)).append(")\n")
                        .append("and ABILITY (")
                        .append(String.format("%.1f", ability)).append(").\n")
                        .append("You’re doing well! This is just a gentle nudge to keep going.\n")
                        .append("\nDon’t forget to log your progress for the day:\n");
                break;
        }

        sb.append("\nBest of luck,\n");
        sb.append("Your Productivity App\n");
        return sb.toString();
    }

    private void appendSparkData(StringBuilder sb, UserDao user) {
        List<Object[]> tasksPerGoal = taskRepo.findTodoTasksPerGoal(user.getId());
        for (Object[] row : tasksPerGoal) {
            String goalName = (String) row[0];
            Long todoCount = (Long) row[1];
            sb.append(" - Goal: ").append(goalName).append(", To-Do Tasks: ").append(todoCount).append("\n");
        }
    }

    private void appendFacilitatorData(StringBuilder sb, UserDao user) {
        List<Task> topTasks = mainKanbanService.getTaskPrioritisation();
        for (Task task: topTasks) {
            sb.append(" - ")
                    .append(task.name())
                    .append(" (")
                    .append(task.description() != null ? task.description() : "No description")
                    .append(")\n");
        }
        sb.append("\nNeed help? Click here to chat with our chatbot: https://app.example.com/chatbot\n");
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mindmapdissertation@gmail.com");  // or your email
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        System.out.println("Email sent to " + to + " with subject: " + subject);
    }
}
