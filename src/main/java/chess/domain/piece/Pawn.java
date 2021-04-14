package chess.domain.piece;

public final class Pawn extends Piece {

    private static final String INITIAL_NAME = "P";

    public Pawn(final Team team) {
        super(team, INITIAL_NAME);
    }

}
