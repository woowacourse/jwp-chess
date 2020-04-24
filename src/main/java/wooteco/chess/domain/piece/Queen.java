package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class Queen extends Piece {

    private static final int INITIAL_BLACK_X = 8;
    private static final int INITIAL_WHITE_X = 1;
    private static final int INITIAL_Y = 4;
    private static final double SCORE = 9;
    private static final String NAME = "queen";

    public Queen(final Team team) {
        super(NAME, team, SCORE);
    }

    @Override
    public boolean isMovable(final Path path) {
        return (path.isStraight() || path.isDiagonal())
                && (path.isEndEmpty() || path.isEnemyOnEnd())
                && !path.isBlocked();
    }

    @Override
    public boolean isInitialPosition(final Position position) {
        if (team == Team.WHITE) {
            return position.equals(Position.of(INITIAL_WHITE_X, INITIAL_Y));
        }
        return position.equals(Position.of(INITIAL_BLACK_X, INITIAL_Y));
    }
}
