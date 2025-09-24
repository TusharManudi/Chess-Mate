package com.chess.chess.service;

import com.chess.chess.dto.JoinRequest;
import com.chess.chess.dto.JoinResponse;
import com.chess.chess.models.Game;
import com.chess.chess.models.GameStatus;
import com.chess.chess.models.User;
import com.chess.chess.repo.GameRepo;
import com.chess.chess.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class GameService {
    private final UserRepo userRepo;
    private final GameRepo gameRepo;

    private final Random random = new Random();

    private static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Transactional
    public JoinResponse joinOrCreateGame(UUID userId) {
        User player = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found")) ;

        Optional<Game> optWaiting = gameRepo.findFirstWaitingPartialGameForUpdate(GameStatus.WAITING) ;
        if(optWaiting.isPresent()) {
            Game game =  optWaiting.get();

            if((game.getWhite() != null && game.getWhite().getId().equals(userId) ) ||
                    (game.getBlack() != null && game.getBlack().getId().equals(userId)) )
            {
                    throw new RuntimeException("user already in game ");
            }

            String assignedColor ;
            if(game.getWhite() != null && game.getBlack() == null){
                game.setBlack(player) ;
                assignedColor = "Black";
            } else if (game.getBlack() != null && game.getWhite() == null) {
                game.setWhite(player) ;
                assignedColor = "White";
            }else{
                throw new RuntimeException("Cannot join the game") ;
            }
            game.setStatus(GameStatus.IN_GAME) ;
            if(game.getStartTime() == null) {
                game.setStartTime(LocalDateTime.now()) ;
            }
            gameRepo.save(game) ;

            UUID opponentId = assignedColor.equals("Black") ? game.getWhite().getId() : game.getBlack().getId();

            return JoinResponse.builder()
                    .gameId(game.getId())
                    .assignedColor(assignedColor)
                    .gameStatus(game.getStatus())
                    .fen(game.getFen())
                    .opponentId(opponentId)
                    .build() ;

        }else {
            Game newGame = new Game();
            newGame.setStatus(GameStatus.WAITING);
            newGame.setFen(STARTING_FEN);
            newGame.setStartTime(LocalDateTime.now());
            newGame.setMoves(new ArrayList<>());
            newGame.setResult(null);
            String assignedColor;
            if (random.nextBoolean()) {
                assignedColor = "Black";
                newGame.setBlack(player);
            } else {
                assignedColor = "White";
                newGame.setWhite(player);
            }

            gameRepo.save(newGame) ;

            return JoinResponse.builder()
                    .gameId(newGame.getId())
                    .assignedColor(assignedColor)
                    .fen(STARTING_FEN)
                    .opponentId(null)
                    .gameStatus(newGame.getStatus())
                    .build() ;
        }
    }

    public Optional<Game> getGame(UUID gameId) {
        return gameRepo.findById(gameId) ;
    }

    public Game save(Game game) {
        return gameRepo.save(game) ;
    }
}
