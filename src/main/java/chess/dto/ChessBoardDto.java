package chess.dto;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.Map;

public class ChessBoardDto {
    private final Map<Position, Piece> chessBoard;
    private final String currentTurn;

    public ChessBoardDto(final Map<Position, Piece> chessBoard, final String currentTurn) {
        this.chessBoard = chessBoard;
        this.currentTurn = currentTurn;
    }

    public Map<Position, Piece> getChessBoard() {
        return chessBoard;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }
}
