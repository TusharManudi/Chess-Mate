package com.chess.chess.controller;

import com.chess.chess.dto.GameOverNotification;
import com.chess.chess.dto.MoveRequest;
import com.chess.chess.dto.MoveResponse;
import com.chess.chess.models.Game;
import com.chess.chess.models.GameStatus;
import com.chess.chess.models.User;
import com.chess.chess.repo.GameRepo;
import com.chess.chess.repo.UserRepo;
import com.chess.chess.service.ChessService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class MoveController {
    private final ChessService chessService;

    private final GameRepo gameRepo ;
    private final UserRepo userRepo;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/move")
    public void makeMove(@Payload MoveRequest moveRequest , Principal principal){

        String email = principal.getName();
        User player = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("In the move controller "+ moveRequest.getGameId() + "from "+moveRequest.getFrom()+" to "+moveRequest.getTo());
        Game game = gameRepo.findById(moveRequest.getGameId()).get();

        if(!player.getId().equals(game.getWhite().getId()) &&
                !player.getId().equals(game.getBlack().getId())) {
            messagingTemplate.convertAndSend(
                    "/topic/errors-" + player.getId(),
                    "You are not part of this game"
            );
            return;
        }


        boolean isValid = chessService.isValidMove(moveRequest.getFen(), moveRequest.getFrom(), moveRequest.getTo(), moveRequest.getPromotion());

        if(!isValid){
            System.out.println("In the move controller if the move is invalid "+ moveRequest.getGameId() + "from "+moveRequest.getFrom()+" to "+moveRequest.getTo());
            messagingTemplate.convertAndSend(
                    "/topic/errors-" + player.getId(),
                    "Invalid move"
            );
            return;
        }

        String fenAfterMove = chessService.applyMove(moveRequest.getFen(), moveRequest.getFrom(), moveRequest.getTo(), moveRequest.getPromotion());
        System.out.println("In the move controller if the move is valid "+ moveRequest.getGameId() + "from "+moveRequest.getFrom()+" to "+moveRequest.getTo());

        // Check game ending conditions
        boolean isCheck = chessService.isInCheck(fenAfterMove);
        boolean isCheckmate = chessService.isCheckMate(fenAfterMove);
        boolean isStalemate = chessService.isStaleMate(fenAfterMove);

        // Update game status and result if game is over
        String gameResult = null;
        if (isCheckmate) {
            game.setStatus(GameStatus.FINISHED);
            // The player who just moved wins (since opponent is in checkmate)
            if (player.getId().equals(game.getWhite().getId())) {
                gameResult = "WHITE_WINS";
            } else {
                gameResult = "BLACK_WINS";
            }
            game.setResult(gameResult);
            game.setEndTime(LocalDateTime.now());
        } else if (isStalemate) {
            game.setStatus(GameStatus.FINISHED);
            gameResult = "DRAW_STALEMATE";
            game.setResult(gameResult);
            game.setEndTime(LocalDateTime.now());
        }

        // Update the game in database
        game.setFen(fenAfterMove);
        gameRepo.save(game);

        // Create move response
        MoveResponse moveResponse = MoveResponse.builder()
                .gameId(moveRequest.getGameId())
                .from(moveRequest.getFrom())
                .to(moveRequest.getTo())
                .promotion(moveRequest.getPromotion())
                .fen(fenAfterMove)
                .isCheck(isCheck)
                .isCheckmate(isCheckmate)
                .isStalemate(isStalemate)
                .gameResult(gameResult)
                .gameStatus(String.valueOf(game.getStatus()))
                .build();

        // Broadcast to both players
        messagingTemplate.convertAndSend(
                "/topic/move-update-" + game.getWhite().getId(),
                moveResponse
        );

        messagingTemplate.convertAndSend(
                "/topic/move-update-" + game.getBlack().getId(),
                moveResponse
        );

        // If game is over, send game over notification
        if (isCheckmate || isStalemate) {
            GameOverNotification gameOverNotification = GameOverNotification.builder()
                    .gameId(game.getId())
                    .result(gameResult)
                    .winner(isCheckmate ? player.getId() : null)
                    .reason(isCheckmate ? "Checkmate" : "Stalemate")
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
}
