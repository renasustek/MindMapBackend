package com.github.renas.persistance.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "chatbot_messages", schema = "mind_map")
public class ChatbotDao {
    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID chatbotMessageId;

    @Column(name = "message", columnDefinition = "VARCHAR(255)", nullable = false, unique = true, length = 255)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String message;

    @Column(name = "sentiment_score", columnDefinition = "DECIMAL(5,4)", nullable = false)
    @JdbcTypeCode(SqlTypes.DECIMAL)
    private BigDecimal sentimentScore;
    public ChatbotDao() {
    }

    public UUID getChatbotMessageId() {
        return chatbotMessageId;
    }

    public void setChatbotMessageId(UUID chatbotMessageId) {
        this.chatbotMessageId = chatbotMessageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(BigDecimal sentimentScore) {
        this.sentimentScore = sentimentScore;
    }
}
