package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class Queen implements Piece{
    private static final int INITIAL_BLACK_X = 1;
    private static final int INITIAL_WHITE_X = 8;
    private static final int INITIAL_Y = 4;

    private final Team team;
    private final double score = 9;

    public Queen(Team team) {
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
