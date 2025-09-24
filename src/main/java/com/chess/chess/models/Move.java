package com.chess.chess.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class Move{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "gameId" , nullable = false)
    private Game game ;

    private int move_no ;
    private String fromSquare ;
    private String toSquare ;
    private String piece ;
    private String pieceCapture ;
    private String promotion ;
    private boolean inCheck ;
    private boolean check_mate ;

    private LocalDateTime timestamp ;

    private String fenAfterMove ;
}
