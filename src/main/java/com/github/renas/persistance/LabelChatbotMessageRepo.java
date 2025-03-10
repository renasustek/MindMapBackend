package com.github.renas.persistance;

import com.github.renas.persistance.models.KanbanBoardDao;
import com.github.renas.persistance.models.LabelChatbotMessageDao;
import com.github.renas.persistance.models.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.github.renas.security.CurrentUserId.getLoggedInUserId;

@Repository
public interface LabelChatbotMessageRepo extends JpaRepository<LabelChatbotMessageDao, UUID> {

    @Query("SELECT x.chatbotMessageId FROM LabelChatbotMessageDao x WHERE x.labelId = :labelUuid AND x.userId = :userId")
    List<UUID> findChatbotIdUsingLabelId(UUID labelUuid, UUID userId);

    default List<UUID> findChatbotIdUsingLabelIdForCurrentUser(UUID labelUuid) {
        return findChatbotIdUsingLabelId(labelUuid,getLoggedInUserId());
    }

}
