package spring.entity.converter;

import spring.chess.board.ChessBoard;
import spring.chess.location.Location;
import spring.chess.piece.type.Piece;
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
