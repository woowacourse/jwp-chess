package chess.domain.piece;

import chess.domain.position.Position;

public final class Blank extends Piece {

    private static final String INITIAL_NAME = ".";

    public Blank(final Position position) {
        super(Color.NOTHING, INITIAL_NAME, position);
    }
}
