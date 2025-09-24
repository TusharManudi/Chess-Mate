package com.chess.chess.repo;

import com.chess.chess.models.Move;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoveRepo extends JpaRepository<Move , Integer> {
}
