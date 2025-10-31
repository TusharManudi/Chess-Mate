package com.chess.chess.controller;

import com.chess.chess.dto.GameOverNotification;
import com.chess.chess.models.Game;
import com.chess.chess.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/resignation")
    public void Resignation(@RequestParam  UUID loser ,@RequestParam UUID gameId){

        Game game = gameService.setResignation(loser, gameId) ;
        GameOverNotification gameOverNotification = GameOverNotification.builder()
                .gameId(game.getId())
                .result(game.getResult())
                .winner(loser.equals(game.getWhite().getId()) ? game.getBlack().getId() : game.getWhite().getId())
                .reason("Resignation")
                .build();

        messagingTemplate.convertAndSend(
                "/topic/game-over-" + game.getWhite().getId(),
                gameOverNotification
        );

        messagingTemplate.convertAndSend(
                "/topic/game-over-" + game.getBlack().getId(),
                gameOverNotification
        );
    }

}
