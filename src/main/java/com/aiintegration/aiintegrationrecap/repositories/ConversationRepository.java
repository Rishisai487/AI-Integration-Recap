package com.aiintegration.aiintegrationrecap.repositories;

import com.aiintegration.aiintegrationrecap.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {
    Optional<Conversation> findByIdAndUser_Id(Long aLong, Long id);
}
