package com.chess.chess.service;

import com.chess.chess.dto.MoveRequest;
import com.chess.chess.models.Game;
import com.chess.chess.models.Move;
import com.chess.chess.repo.GameRepo;
import com.chess.chess.repo.MoveRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MoveService {

    private final MoveRepo moveRepo;
    private final GameRepo gameRepo;

    @Transactional
    public void persistMoveAfterValidation(UUID gameId , MoveRequest moveRequest , String fenAfterMove){
        Game game = gameRepo.findById(gameId).orElse(null);
        if(game != null && game.getMoves() == null){
            game.setMoves(new ArrayList<>());
        }
        Move move = new Move() ;
        move.setGame(game) ;
        move.setMove_no(game.getMoves() == null ? 1 : game.getMoves().size()+1);
        move.setFromSquare(moveRequest.getFrom());
        move.setToSquare(moveRequest.getTo());
        move.setPromotion(moveRequest.getPromotion());
        move.setFenAfterMove(fenAfterMove);
        moveRepo.save(move);
//        game.getMoves().add(move);
//        gameRepo.save(game);
    }
}
