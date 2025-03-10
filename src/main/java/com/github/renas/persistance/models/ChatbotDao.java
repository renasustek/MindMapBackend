package com.github.renas.persistance.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "chatbot_messages", schema = "mind_map")
public class ChatbotDao {

    @Id
    @Column(name = "uuid", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID uuid;

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID userId;

    @Column(name = "message", nullable = false, length = 255)
    private String message;

    @Column(name = "sentiment_score", nullable = false)
    private Float sentimentScore;

    @Temporal(TemporalType.DATE)
    @Column(name = "entry_date")
    private Date entryDate;

    public ChatbotDao() {}

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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
