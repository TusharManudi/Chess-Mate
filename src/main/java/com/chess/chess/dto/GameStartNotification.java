package com.chess.chess.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class GameStartNotification {
    private UUID gameId ;
    private String yourColor ;
    private String fen ;
    private UUID opponentId ;
}
