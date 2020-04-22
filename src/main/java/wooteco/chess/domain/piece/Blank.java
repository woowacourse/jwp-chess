package wooteco.chess.domain.piece;

import wooteco.chess.domain.position.PositionFactory;

public class Blank extends Piece {
    public static final String BLANK_DEFAULT_POSITION = "a1";

    public Blank() {
        super(PositionFactory.of(BLANK_DEFAULT_POSITION), PieceType.BLANK, Color.NONE);
    }
}
