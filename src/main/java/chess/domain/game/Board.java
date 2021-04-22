package chess.domain.game;

import chess.domain.location.Position;
import chess.domain.piece.Color;
import chess.domain.piece.Empty;
import chess.domain.piece.Piece;
import chess.exception.ChessException;
import chess.exception.ErrorCode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private final Map<Position, Piece> pieceByPosition;
    private boolean isKingDead = false;

    public Board(Map<Position, Piece> maps) {
        this.pieceByPosition = new HashMap<>(maps);
    }

    public void move(Color color, Position from, Position to) {
        validateFromPiece(color, from);
        validateToPiece(color, to);
        validateCanMove(from, to);
        updateIsKingDead(to);
        movePiece(from, to);
    }

    private void validateFromPiece(Color color, Position from) {
        if (!pieceByPosition.get(from)
                            .isSame(color)) {
            throw new ChessException(ErrorCode.INVALID_PIECE_SELECT);
        }
    }

    private void validateToPiece(Color color, Position to) {
        if (pieceByPosition.get(to)
                           .isSame(color)) {
            throw new ChessException(ErrorCode.CANNOT_KILL_SAME_COLOR);
        }
    }

    private void validateCanMove(Position from, Position to) {
        if (!movablePositions(from).contains(to)) {
            throw new ChessException(ErrorCode.INVALID_MOVE);
        }
    }

    private List<Position> movablePositions(Position from) {
        return pieceByPosition.get(from)
                              .movablePositions(from, allPieces());
    }

    private void updateIsKingDead(Position to) {
        if (pieceByPosition.get(to)
                           .isKing()) {
            isKingDead = true;
        }
    }

    private void movePiece(Position from, Position to) {
        pieceByPosition.put(to, pieceByPosition.get(from));
        pieceByPosition.put(from, new Empty());
    }

    public boolean isKingDead() {
        return isKingDead;
    }

    public Map<Position, Piece> allPieces() {
        return Collections.unmodifiableMap(pieceByPosition);
    }
}