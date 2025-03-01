package com.github.renas.persistance;
import com.github.renas.persistance.models.ChatbotDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ChatbotRepo extends JpaRepository<ChatbotDao, UUID> {
}