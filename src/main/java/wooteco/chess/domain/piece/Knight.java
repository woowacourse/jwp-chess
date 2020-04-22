package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class Knight extends Piece {
    private static final int INITIAL_BLACK_X = 8;
    private static final int INITIAL_WHITE_X = 1;
    private static final int INITIAL_LEFT_Y = 2;
    private static final int INITIAL_RIGHT_Y = 7;
    private static final double SCORE = 2.5;
    private static final int MOVABLE_SIZE = 5;

    public Knight(Team team) {
        super("knight", team, SCORE);
    }

    @Override
    public boolean isMovable(Path path) {
        return path.distanceSquare() == MOVABLE_SIZE
                && (path.isEndEmpty() || path.isEnemyOnEnd());
    }

    @Override
    public boolean isInitialPosition(Position position) {
        if (team == Team.WHITE) {
            return position.equals(Position.of(INITIAL_WHITE_X, INITIAL_LEFT_Y))
                    || position.equals(Position.of(INITIAL_WHITE_X, INITIAL_RIGHT_Y));
        }
        return position.equals(Position.of(INITIAL_BLACK_X, INITIAL_LEFT_Y))
                || position.equals(Position.of(INITIAL_BLACK_X, INITIAL_RIGHT_Y));
    }
}
