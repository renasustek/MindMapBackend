package com.github.renas.persistance;

import com.github.renas.persistance.models.LabelChatbotMessageDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LabelChatbotMessageRepo extends JpaRepository<LabelChatbotMessageDao, UUID> {
}
