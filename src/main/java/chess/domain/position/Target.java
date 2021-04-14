package chess.domain.position;

import chess.domain.piece.Piece;

public class Target {
    private final Piece piece;

    private Target(final Piece piece) {
        this.piece = piece;
    }

    public static Target valueOf(final Piece piece) {
        return new Target(piece);
    }

    public Piece getPiece() {
        return piece;
    }
}
