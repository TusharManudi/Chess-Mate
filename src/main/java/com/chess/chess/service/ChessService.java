package com.chess.chess.service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    public boolean isValidMove(String fen , String from , String to , String promotion){
        try{
            Board board = new Board();
            board.loadFromFen(fen);
            Square fromsquare = Square.valueOf(from.toUpperCase()) ;
            Square tosquare = Square.valueOf(to.toUpperCase()) ;
            //Move from chesslib library and not models
            Move move  ;
            if(promotion!= null && !promotion.isEmpty()){
                Piece promotionPiece = Piece.valueOf(promotion.toUpperCase()) ;
                move = new Move(fromsquare, tosquare, promotionPiece);
            }else {
                move = new Move(fromsquare,tosquare);
            }
            if(board.legalMoves().contains(move)){
                board.doMove(move);
                return true ;
            }
            return false;
        }catch(IllegalArgumentException e){
            return false ;
        }
    }

    public String applyMove(String fen , String from , String to , String promotion){

            Board board = new Board();
            board.loadFromFen(fen) ;
            Square fromsquare = Square.valueOf(from.toUpperCase()) ;
            Square tosquare = Square.valueOf(to.toUpperCase()) ;
            Move move  ;
            if(promotion!= null && !promotion.isEmpty()){
                Piece promotionPiece = Piece.valueOf(promotion.toUpperCase()) ;
                move = new Move(fromsquare, tosquare, promotionPiece);
            }else{
                move = new Move(fromsquare,tosquare);
            }
            board.doMove(move);
            return board.getFen();
    }

    public boolean isInCheck(String fen){
        Board board = new Board();
        board.loadFromFen(fen);
        return board.isKingAttacked();
    }
    public boolean isCheckMate(String fen){
        Board board = new Board();
        board.loadFromFen(fen);
        return board.isMated();
    }
    public boolean isStaleMate(String fen){
        Board board = new Board();
        board.loadFromFen(fen);
        return board.isStaleMate() ;
    }
}
