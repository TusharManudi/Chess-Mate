package com.chess.chess.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class MoveResponse {
    private UUID gameId;
    private String from;
    private String to;
    private String promotion;
    private String fen;
    private boolean isCheck;
    private boolean isCheckmate;
    private boolean isStalemate;
    private String gameResult ;
    private String gameStatus ;
}
