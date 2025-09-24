package com.chess.chess.repo;

import com.chess.chess.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    public Optional<User> findByEmail(String email);
}
