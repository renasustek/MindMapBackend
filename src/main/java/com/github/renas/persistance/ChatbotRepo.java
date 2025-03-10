package com.github.renas.persistance;

import com.github.renas.persistance.models.ChatbotDao;
import com.github.renas.persistance.models.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.github.renas.security.CurrentUserId.getLoggedInUserId;

@Repository
public interface ChatbotRepo extends JpaRepository<ChatbotDao, UUID> {

    @Query("SELECT t.sentimentScore FROM ChatbotDao t WHERE t.uuid = :chatbotMessageId AND t.userId = :userId")
    Float findSentimentScore(@Param("chatbotMessageId") UUID chatbotMessageId, @Param("userId") UUID userId);

    default Float findSentimentScoreForCurrentUser(UUID chatbotMessageId) {
        return findSentimentScore(chatbotMessageId, getLoggedInUserId());
    }

    @Query("SELECT AVG(c.sentimentScore) FROM ChatbotDao c WHERE c.entryDate BETWEEN :startDate AND :endDate AND c.userId = :userId")
    Double findAverageSentimentScore(LocalDate startDate, LocalDate endDate, UUID userId);

    default Double findAverageSentimentScoreForCurrentUser(LocalDate startDate, LocalDate endDate) {
        return findAverageSentimentScore(startDate, endDate, getLoggedInUserId());
    }

}
