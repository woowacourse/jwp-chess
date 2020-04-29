package spring.entity.converter;

import chess.board.ChessBoard;
import chess.location.Location;
import chess.piece.type.Piece;
import spring.entity.PieceEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChessBoardConverter {
    public static ChessBoard convert(Set<PieceEntity> pieceEntities) {
        Map<Location, Piece> board = new HashMap<>();
        for (PieceEntity pieceEntity : pieceEntities) {
            board.put(pieceEntity.toLocation(), pieceEntity.toPiece());
        }

        return new ChessBoard(board);
    }
}
