package com.github.renas.persistance;
import com.github.renas.persistance.models.ChatbotDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatbotRepo extends JpaRepository<ChatbotDao, UUID> {

    @Query("SELECT t.sentimentScore FROM ChatbotDao t WHERE t.chatbotMessageId = :chatbotMessageId")
    Float findSentimentScore(UUID chatbotMessageId);

}