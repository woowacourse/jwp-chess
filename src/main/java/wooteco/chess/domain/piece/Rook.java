package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class Rook extends Piece {
    private static final int INITIAL_BLACK_X = 1;
    private static final int INITIAL_WHITE_X = 8;
    private static final int INITIAL_LEFT_Y = 1;
    private static final int INITIAL_RIGHT_Y = 8;
    private static final double SCORE = 5;


    public Rook(Team team) {
        super(team, SCORE);
    }

    @Override
    public boolean isMovable(Path path) {
        return path.isStraight()
                && (path.isEndEmpty() || path.isEnemyOnEnd())
                && !path.isBlocked();
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
