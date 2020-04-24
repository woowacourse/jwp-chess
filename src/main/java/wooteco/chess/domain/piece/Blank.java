package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class Blank extends Piece {

    private static final int INITIAL_X_THREE = 3;
    private static final int INITIAL_X_FOUR = 4;
    private static final int INITIAL_X_FIVE = 5;
    private static final int INITIAL_X_SIX = 6;
    private static final double SCORE = 0;
    private static final String NAME = "blank";

    public Blank() {
        super(NAME, null, SCORE);
    }

    @Override
    public boolean isMovable(final Path path) {
        return false;
    }

    @Override
    public boolean isInitialPosition(final Position position) {
        return position.isOnX(INITIAL_X_THREE)
                || position.isOnX(INITIAL_X_FOUR)
                || position.isOnX(INITIAL_X_FIVE)
                || position.isOnX(INITIAL_X_SIX);
    }

    @Override
    public boolean isTeamOf(final Team team) {
        return false;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
