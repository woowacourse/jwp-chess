package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Blank;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;

import java.util.Map;

public class Path {
    private Map<Position, Piece> path;
    private Position start;
    private Position end;

    public Path(Map<Position, Piece> path, Position start, Position end) {
        validate(path, start);
        this.path = path;
        this.start = start;
        this.end = end;
    }

    private void validate(Map<Position, Piece> path, Position start) {
        if (!path.containsKey(start)) {
            throw new IllegalArgumentException("시작점을 포함하지 않습니다.");
        }
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
        return start.xDistance(end) == 0
                || start.yDistance(end) == 0;
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
