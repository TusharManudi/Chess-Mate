package com.chess.chess.repo;

import com.chess.chess.models.Game;
import com.chess.chess.models.GameStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface GameRepo extends JpaRepository<Game, UUID> {
    // Find a waiting game that has exactly one player assigned (either white or black) and lock it for update

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM Game g WHERE g.status = :status AND ((g.white IS NOT NULL AND g.black IS NULL) OR (g.black IS NOT NULL AND g.white IS NULL))")
    Optional<Game> findFirstWaitingPartialGameForUpdate(GameStatus status);
}
