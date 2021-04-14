package chess.domain.piece;

import chess.domain.position.Position;

public final class Bishop extends GeneralPiece {

    private static final String INITIAL_NAME = "B";

    public Bishop(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
    }
}
