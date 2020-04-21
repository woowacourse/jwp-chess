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


    public Pawn(Team team) {
        super(team, SCORE);
    }

    @Override
    public boolean isMovable(Path path) {
        if (path.isEnemyOnEnd()) {
            return path.distanceSquare() == ATTACK_MOVABLE_SIZE
                    && path.headingForward();
        }

        if (path.isOnInitialPosition()) {
            return (path.distanceSquare() == INITIAL_MOVABLE_SIZE || path.distanceSquare() == MOVABLE_SIZE)
                    && path.headingForward()
                    && path.isEndEmpty();
        }

        return path.distanceSquare() == MOVABLE_SIZE
                && path.headingForward() && path.isEndEmpty();
    }

    @Override
    public boolean isInitialPosition(Position position) {
        if (team == Team.WHITE) {
            return position.isOnX(INITIAL_WHITE_X);
        }
        return position.isOnX(INITIAL_BLACK_X);
    }
}
