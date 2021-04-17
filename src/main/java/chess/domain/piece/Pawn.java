package chess.domain.piece;

import chess.domain.board.*;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends AbstractPiece {

    private static final String NAME = "pawn";
    public static final double POINT = 1;
    public static final int BLACK_PAWN_START_LINE = 7;
    public static final int WHITE_PAWN_START_LINE = 2;
    public static final int MOVE_FIRST_RANGE = 2;
    public static final int MOVE_DEFAULT_RANGE = 1;
    private static final int DIAGONAL_MOVE_RANGE = 2;

    public Pawn(Team team) {
        super(team, NAME);
    }

    @Override
    public Strategy strategy() {
        if (isBlackTeam()) {
            return new Strategy(Direction.blackPawnDirection(), MOVE_FIRST_RANGE);
        }
        return new Strategy(Direction.whitePawnDirection(), MOVE_FIRST_RANGE);
    }

    @Override
    public String getCharName() {
        if (isBlackTeam()) {
            return "P";
        }
        return "p";
    }

    @Override
    public boolean isKing() {
        return false;
    }

    @Override
    public boolean isPawn() {
        return true;
    }

    @Override
    public double getPoint() {
        return POINT;
    }

    @Override
    public List<Position> generate(Path path, boolean target) {
        final Direction direction = path.computeDirection();
        final Strategy strategy = strategy();
        strategy.moveTowards(direction);
        final int distance = path.computeDistance();
        if (path.isDiagonal()) {
            validateDiagonalMove(target, strategy, distance);
            return new ArrayList<>();
        }
        validateMoveRange(distance);
        if (distance == Pawn.MOVE_FIRST_RANGE) {
            path.validateSourceLocatedAtStartLine();
        }
        return super.generatePaths(path, direction, distance + 2);
    }

    private void validateMoveRange(int distance) {
        if (distance > Pawn.MOVE_FIRST_RANGE) {
            throw new IllegalArgumentException("[ERROR] 폰은 두 칸 초과 움직일 수 없습니다.");
        }
    }

    private void validateDiagonalMove(boolean target, Strategy strategy, int distance) {
        if (!target) {
            throw new IllegalArgumentException("[ERROR] 폰은 대각선에 상대팀의 말이 있는 경우 한 칸 이동할 수 있습니다.");
        }
        validateDistance(distance, strategy.moveRange());
    }

    private void validateDistance(int distance, int moveRange) {
        if (distance > moveRange) {
            throw new IllegalArgumentException("[ERROR] 이동할 수 있는 거리를 벗어났습니다.");
        }
    }
}
