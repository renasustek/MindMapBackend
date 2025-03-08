package com.github.renas.persistance;
import com.github.renas.persistance.models.ChatbotDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface ChatbotRepo extends JpaRepository<ChatbotDao, UUID> {

    @Query("SELECT t.sentimentScore FROM ChatbotDao t WHERE t.chatbotMessageId = :chatbotMessageId")
    Float findSentimentScore(UUID chatbotMessageId);

    @Query("SELECT AVG(c.sentimentScore) FROM ChatbotDao c WHERE c.entryDate BETWEEN :startDate AND :endDate")
    Double findAverageSentimentScore(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


}