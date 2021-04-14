package chess.domain.piece;

public final class Queen extends GeneralPiece {

    private static final String INITIAL_NAME = "Q";

    public Queen(final Team team) {
        super(team, INITIAL_NAME);
    }
}
