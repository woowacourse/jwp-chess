package chess.model;

import chess.model.piece.Empty;
import chess.model.piece.Piece;
import chess.model.position.Position;

import java.util.Map;

public class MoveResult {
    private final Piece sourcePiece;
    private final Position sourcePosition;
    private final Piece targetPiece;
    private final Position targetPosition;
    private final Turn turn;

    private static final Piece empty = new Empty();

    public MoveResult(Position sourcePosition, Position targetPosition, Piece targetPiece, Turn turn) {
        this.sourcePosition = sourcePosition;
        this.sourcePiece = empty;
        this.targetPosition = targetPosition;
        this.targetPiece = targetPiece;
        this.turn = turn.change();
    }

    public Piece getSourcePiece() {
        return sourcePiece;
    }

    public String getSourcePosition() {
        return sourcePosition.getPosition();
    }

    public Piece getTargetPiece() {
        return targetPiece;
    }

    public String getTargetPosition() {
        return targetPosition.getPosition();
    }

    public Turn getTurn() {
        return turn;
    }
}
