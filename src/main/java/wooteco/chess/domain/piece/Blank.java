package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Coordinate;
import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class Blank extends Piece {

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
        return position.isOnX(Coordinate.THREE)
                || position.isOnX(Coordinate.FOUR)
                || position.isOnX(Coordinate.FIVE)
                || position.isOnX(Coordinate.SIX);
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
