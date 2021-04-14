package chess.domain.piece;

public final class Bishop extends GeneralPiece {

    private static final String INITIAL_NAME = "B";

    public Bishop(final Team team) {
        super(team, INITIAL_NAME);
    }
}
