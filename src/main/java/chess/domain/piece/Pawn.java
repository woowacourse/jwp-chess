package chess.domain.piece;

public final class Pawn extends Piece {

    private static final String INITIAL_NAME = "P";

    public Pawn(final Color color) {
        super(color, INITIAL_NAME);
    }

}
