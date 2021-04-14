package chess.domain.piece;

import chess.domain.position.Position;

public final class Knight extends GeneralPiece {

    private static final String INITIAL_NAME = "N";

    public Knight(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
    }

}
