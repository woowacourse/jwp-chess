package chess.model.board;

import chess.model.piece.Piece;
import java.util.Map;

public final class MoveResult {
    private final Map<Square, Piece> affectedPiece;
    private final Piece removedPiece;

    public MoveResult(Map<Square, Piece> affectedPiece,
                      Piece removedPiece) {
        this.affectedPiece = affectedPiece;
        this.removedPiece = removedPiece;
    }

    public static MoveResult from(Square from, Square to, Piece fromPiece, Piece toPiece, Piece removedPiece) {
        return new MoveResult(Map.of(from, fromPiece, to, toPiece), removedPiece);
    }

    public boolean isKingAttacked() {
        return removedPiece.isKing();
    }

    public Map<Square, Piece> getAffectedPiece() {
        return affectedPiece;
    }
}
