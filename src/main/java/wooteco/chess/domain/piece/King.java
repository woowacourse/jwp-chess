package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Coordinate;
import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class King extends Piece {

    private static final double SCORE = 0;
    private static final int MAX_MOVABLE_SIZE = 2;
    private static final String NAME = "king";

    public King(final Team team) {
        super(NAME, team, SCORE);
    }

    @Override
    public boolean isMovable(final Path path) {
        return path.distanceSquare() <= MAX_MOVABLE_SIZE
                && (path.isEmptyOnEnd() || path.isEnemyOnEnd());
    }

    @Override
    public boolean isInitialPosition(final Position position) {
        if (team == Team.WHITE) {
            return position.equals(Position.of(Coordinate.ONE, Coordinate.FIVE));
        }
        return position.equals(Position.of(Coordinate.EIGHT, Coordinate.FIVE));
    }
}
