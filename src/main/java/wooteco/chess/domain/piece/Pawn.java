package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

public class Pawn implements Piece{
    private static final int INITIAL_BLACK_X = 2;
    private static final int INITIAL_WHITE_X = 7;

    private final Team team;
    private final double score = 1;

    public Pawn(Team team) {
        this.team = team;
    }

    @Override
    public boolean isMovable(Path path) {
        return false;
    }

    @Override
    public boolean isInitialPosition(Position position) {
        if (team == Team.WHITE) {
            return position.isOnX(INITIAL_WHITE_X);
        }
        return position.isOnX(INITIAL_BLACK_X);
    }
}
