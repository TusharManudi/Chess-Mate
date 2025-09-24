package com.chess.chess.controller;

import com.chess.chess.dto.GameStartNotification;
import com.chess.chess.dto.JoinRequest;
import com.chess.chess.dto.JoinResponse;
import com.chess.chess.models.GameStatus;
import com.chess.chess.models.User;
import com.chess.chess.repo.UserRepo;
import com.chess.chess.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class SocketController {
    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepo userRepo;


    @MessageMapping("/join")
    public void handleJoinGame(JoinRequest request, Principal principal) {
        String email = principal.getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        JoinResponse response = gameService.joinOrCreateGame(user.getId());

        // Send join response to topic instead of user queue
        messagingTemplate.convertAndSend(
                "/topic/join-response-" + user.getId().toString(),
                response
        );

        // If the game just started (2nd player joined), notify both players
        if (response.getGameStatus() == GameStatus.IN_GAME && response.getOpponentId() != null) {
            // Notify the current player
            GameStartNotification notification = GameStartNotification.builder()
                    .opponentId(response.getOpponentId())
                    .gameId(response.getGameId())
                    .fen(response.getFen())
                    .yourColor(response.getAssignedColor())
                    .build();

            messagingTemplate.convertAndSend(
                    "/topic/game-start-" + request.getUserId().toString(),
                    notification
            );

            // Determine opponent's color
            String opponentColor = response.getAssignedColor().equals("Black") ? "White" : "Black";

            // Notify the opponent
            GameStartNotification opponentNotification = GameStartNotification.builder()
                    .opponentId(user.getId())
                    .gameId(response.getGameId())
                    .fen(response.getFen())
                    .yourColor(opponentColor)
                    .build();

            messagingTemplate.convertAndSend(
                    "/topic/game-start-" + response.getOpponentId().toString(),
                    opponentNotification
            );
        }
    }
}
