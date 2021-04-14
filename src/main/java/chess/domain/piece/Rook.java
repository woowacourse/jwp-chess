package chess.domain.piece;

public final class Rook extends GeneralPiece {

    private static final String INITIAL_NAME = "R";

    public Rook(final Team team) {
        super(team, INITIAL_NAME);
    }

}
