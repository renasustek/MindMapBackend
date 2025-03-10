package com.github.renas.persistance.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "label_chatbotmessage", schema = "mind_map")
public class LabelChatbotMessageDao {

    @Id
    @Column(name = "uuid", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID uuid;

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID userId;


    @Column(name = "label_id", columnDefinition = "CHAR(36)", nullable = false, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID labelId;

    @Column(name = "chatbot_message_id", columnDefinition = "CHAR(36)", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID chatbotMessageId;

    public LabelChatbotMessageDao() {
    }

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

    public UUID getLabelId() {
        return labelId;
    }

    public void setLabelId(UUID labelId) {
        this.labelId = labelId;
    }

    public UUID getChatbotMessageId() {
        return chatbotMessageId;
    }

    public void setChatbotMessageId(UUID chatbotMessageId) {
        this.chatbotMessageId = chatbotMessageId;
    }
}



