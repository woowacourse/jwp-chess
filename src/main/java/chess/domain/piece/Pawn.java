package chess.domain.piece;

import chess.domain.position.Position;

public final class Pawn extends Piece {

    private static final String INITIAL_NAME = "P";

    public Pawn(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
    }

}
