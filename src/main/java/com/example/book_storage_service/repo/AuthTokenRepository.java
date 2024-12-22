package com.example.book_storage_service.repo;

import com.example.book_storage_service.models.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByToken(String token);
    Optional<AuthToken> findByUsername(String username);
}
