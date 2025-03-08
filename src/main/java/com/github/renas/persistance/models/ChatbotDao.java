package com.github.renas.persistance.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Date;
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

    @Column(name = "sentiment_score", columnDefinition = "FLOAT(4)", nullable = false)
    @JdbcTypeCode(SqlTypes.FLOAT)
    private Float sentimentScore;

    @Column(name = "entry_date")
    @Temporal(TemporalType.DATE)
    private Date entryDate;


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

    public Float getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(Float sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }
}
