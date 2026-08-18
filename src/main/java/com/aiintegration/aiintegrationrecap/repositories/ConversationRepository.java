package com.aiintegration.aiintegrationrecap.repositories;

import com.aiintegration.aiintegrationrecap.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {
}
