package com.chess.chess.dto;

import com.chess.chess.models.GameStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class JoinResponse {
    private UUID gameId ;
    private UUID opponentId ;
    private String fen ;
    private String assignedColor ;
    private GameStatus gameStatus ;
}
