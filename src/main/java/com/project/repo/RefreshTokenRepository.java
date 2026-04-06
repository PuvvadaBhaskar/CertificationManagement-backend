package com.project.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.RefreshToken;
import com.project.model.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);
}