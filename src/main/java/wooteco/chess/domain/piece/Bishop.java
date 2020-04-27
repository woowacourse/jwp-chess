package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Coordinate;
import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class Bishop extends Piece {

    private static final double SCORE = 3;
    private static final String NAME = "bishop";

    public Bishop(final Team team) {
        super(NAME, team, SCORE);
    }

    @Override
    public boolean isMovable(final Path path) {
        return path.isDiagonal()
                && (path.isEmptyOnEnd() || path.isEnemyOnEnd())
                && path.isNotBlocked();
    }

    @Override
    public boolean isInitialPosition(final Position position) {
        if (team == Team.WHITE) {
            return position.equals(Position.of(Coordinate.ONE, Coordinate.THREE))
                    || position.equals(Position.of(Coordinate.ONE, Coordinate.SIX));
        }
        return position.equals(Position.of(Coordinate.EIGHT, Coordinate.THREE))
                || position.equals(Position.of(Coordinate.EIGHT, Coordinate.SIX));
    }
}
