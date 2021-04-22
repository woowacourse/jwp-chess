package chess.dto;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.Map;

public class ChessBoardDto {
    private final Map<Position, Piece> chessBoard;

    public ChessBoardDto(final Map<Position, Piece> chessBoard) {
        this.chessBoard = chessBoard;
    }

    public Map<Position, Piece> getChessBoard() {
        return chessBoard;
    }
}
