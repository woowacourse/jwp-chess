package chess.domain.piece;

import chess.domain.position.Position;

public final class Queen extends GeneralPiece {

    private static final String INITIAL_NAME = "Q";

    public Queen(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
    }
}
