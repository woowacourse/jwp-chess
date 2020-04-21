package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class King implements Piece{
    private static final int INITIAL_BLACK_X = 1;
    private static final int INITIAL_WHITE_X = 8;
    private static final int INITIAL_Y = 5;

    private final Team team;
    private final double score = 0;

    public King(Team team) {
        this.team = team;
    }

    @Override
    public boolean isMovable(Path path) {
        return false;
    }

    @Override
    public boolean isInitialPosition(Position position) {
        if (team == Team.WHITE) {
            return position.equals(Position.of(INITIAL_WHITE_X, INITIAL_Y));
        }
        return position.equals(Position.of(INITIAL_BLACK_X, INITIAL_Y));
    }
}
