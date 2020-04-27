package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Coordinate;
import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class Knight extends Piece {

    private static final double SCORE = 2.5;
    private static final int MOVABLE_SIZE = 5;
    private static final String NAME = "knight";

    public Knight(final Team team) {
        super(NAME, team, SCORE);
    }

    @Override
    public boolean isMovable(final Path path) {
        return path.distanceSquare() == MOVABLE_SIZE
                && (path.isEmptyOnEnd() || path.isEnemyOnEnd());
    }

    @Override
    public boolean isInitialPosition(final Position position) {
        if (team == Team.WHITE) {
            return position.equals(Position.of(Coordinate.ONE, Coordinate.TWO))
                    || position.equals(Position.of(Coordinate.ONE, Coordinate.SEVEN));
        }
        return position.equals(Position.of(Coordinate.EIGHT, Coordinate.TWO))
                || position.equals(Position.of(Coordinate.EIGHT, Coordinate.SEVEN));
    }
}
