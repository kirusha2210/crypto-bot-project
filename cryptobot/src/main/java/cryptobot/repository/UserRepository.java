package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUserId(String userId);
}
