package com.aiintegration.aiintegrationrecap.repositories;

import com.aiintegration.aiintegrationrecap.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserName(String username);

    Boolean existsByUserName(String userName);
}
