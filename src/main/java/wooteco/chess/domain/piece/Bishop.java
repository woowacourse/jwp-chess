package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class Bishop implements Piece{
    private static final int INITIAL_BLACK_X = 1;
    private static final int INITIAL_WHITE_X = 8;
    private static final int INITIAL_LEFT_Y = 3;
    private static final int INITIAL_RIGHT_Y = 6;

    private final Team team;
    private final double score= 3;

    public Bishop(Team team) {
        this.team = team;
    }

    @Override
    public boolean isMovable(Path path) {
        return false;
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
