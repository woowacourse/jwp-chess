package chess.domain.piece;

import chess.domain.position.Position;

public final class Blank extends Piece {

    private static final Piece BLANK = new Blank();
    private static final String INITIAL_NAME = ".";

    private Blank() {
        super(Color.NOTHING, INITIAL_NAME, Position.Blank());
    }

    public static Piece getInstance() {
        return BLANK;
    }
}
