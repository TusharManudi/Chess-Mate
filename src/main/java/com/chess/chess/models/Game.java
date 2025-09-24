package com.chess.chess.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id ;


    @ManyToOne
    @JoinColumn(name = "white_player_id")
    private User white ;

    @ManyToOne
    @JoinColumn(name = "black_player_id")
    private User black ;

    @Column(nullable = false)
    private GameStatus status ; //in-game , waiting , finished
    private String result ; //1-0 , 0-1 , 1/2-1/2

    @Column(columnDefinition = "Text")
    private String fen ;

    private LocalDateTime startTime ;
    private LocalDateTime endTime ;

    @OneToMany(mappedBy = "game" ,  cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Move> moves ;
}
