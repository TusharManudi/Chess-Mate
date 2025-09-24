package com.chess.chess.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class GameOverNotification {
    private UUID gameId ;
    private String result ;
    private String reason ;
    private UUID winner ;
}
