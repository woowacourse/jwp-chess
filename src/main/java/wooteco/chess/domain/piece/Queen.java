package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Coordinate;
import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class Queen extends Piece {

    private static final double SCORE = 9;
    private static final String NAME = "queen";

    public Queen(final Team team) {
        super(NAME, team, SCORE);
    }

    @Override
    public boolean isMovable(final Path path) {
        return (path.isStraight() || path.isDiagonal())
                && (path.isEmptyOnEnd() || path.isEnemyOnEnd())
                && path.isNotBlocked();
    }

    @Override
    public boolean isInitialPosition(final Position position) {
        if (team == Team.WHITE) {
            return position.equals(Position.of(Coordinate.ONE, Coordinate.FOUR));
        }
        return position.equals(Position.of(Coordinate.EIGHT, Coordinate.FOUR));
    }
}
