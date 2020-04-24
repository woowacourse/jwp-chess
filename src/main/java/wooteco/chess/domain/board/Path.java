package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Blank;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;

import java.util.Map;

public class Path {

    private static final int TWO = 2;
    private static final int ZERO = 0;

    private Map<Position, Piece> path;
    private Position start;
    private Position end;

    public Path(final Map<Position, Piece> path, final Position start, final Position end) {
        validate(path, start);
        this.path = path;
        this.start = start;
        this.end = end;
    }

    private void validate(final Map<Position, Piece> path, final Position start) {
        if (!path.containsKey(start)) {
            throw new IllegalArgumentException("시작점을 포함하지 않습니다.");
        }
    }

    public double distanceSquare() {
        return Math.pow(start.xDistance(end), TWO) + Math.pow(start.yDistance(end), TWO);
    }

    public boolean isEndEmpty() {
        return path.get(end) instanceof Blank;
    }

    public boolean isEnemyOnEnd() {
        if (isEndEmpty()) {
            return false;
        }

        Piece startPiece = path.get(start);
        Piece endPiece = path.get(end);
        return startPiece.isEnemy(endPiece);
    }

    public boolean isBlocked() {
        return path.entrySet()
                .stream()
                .filter(entry -> entry.getKey() != start && entry.getKey() != end)
                .anyMatch(entry -> !(entry.getValue() instanceof Blank));
    }

    public boolean isStraight() {
        return start.xDistance(end) == ZERO
                || start.yDistance(end) == ZERO;
    }

    public boolean isDiagonal() {
        return start.xDistance(end) == start.yDistance(end);
    }

    public boolean isOnInitialPosition() {
        Piece piece = path.get(start);
        return piece.isInitialPosition(start);
    }

    public boolean headingForward() {
        Piece piece = path.get(start);
        if (piece.isTeamOf(Team.BLACK)) {
            return start.hasLargerX(end);
        }
        return end.hasLargerX(start);
    }
}
