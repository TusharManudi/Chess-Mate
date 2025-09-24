package com.chess.chess.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MoveRequest {
    private UUID gameId ;
    private UUID playerId ;
    private String from ;
    private String to ;
    private String promotion ;
    private String fen;
}
