package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class Pawn extends Piece {

    private static final int INITIAL_BLACK_X = 7;
    private static final int INITIAL_WHITE_X = 2;
    private static final double SCORE = 1;
    private static final int ATTACK_MOVABLE_SIZE = 2;
    private static final int INITIAL_MOVABLE_SIZE = 4;
    private static final int MOVABLE_SIZE = 1;
    private static final String NAME = "pawn";

    public Pawn(final Team team) {
        super(NAME, team, SCORE);
    }

    @Override
    public boolean isMovable(final Path path) {
        if (path.isEnemyOnEnd()) {
            return attackMovable(path);
        }

        if (path.isOnInitialPosition()) {
            return initialMovable(path);
        }

        return normalMovable(path);
    }

    private boolean attackMovable(Path path) {
        return path.distanceSquare() == ATTACK_MOVABLE_SIZE
                && path.headingForward();
    }

    private boolean initialMovable(Path path) {
        return (path.distanceSquare() == INITIAL_MOVABLE_SIZE || path.distanceSquare() == MOVABLE_SIZE)
                && !path.isBlocked()
                && path.headingForward()
                && path.isEndEmpty();
    }

    private boolean normalMovable(Path path) {
        return path.distanceSquare() == MOVABLE_SIZE
                && path.headingForward() && path.isEndEmpty();
    }

    @Override
    public boolean isInitialPosition(final Position position) {
        if (team == Team.WHITE) {
            return position.isOnX(INITIAL_WHITE_X);
        }
        return position.isOnX(INITIAL_BLACK_X);
    }
}
