package chess.domain.piece;

import chess.domain.position.Position;

public final class Rook extends GeneralPiece {

    private static final String INITIAL_NAME = "R";

    public Rook(final Color color, final Position position) {
        super(color, INITIAL_NAME, position);
    }

}
