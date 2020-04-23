package wooteco.chess.domain.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Position {
    private final Coordinate x;
    private final Coordinate y;

    public static List<Position> positions = new ArrayList<>();

    static {
        for (Coordinate x : Coordinate.values()) {
            for (Coordinate y : Coordinate.values()) {
                positions.add(new Position(x, y));
            }
        }
    }

    private Position(Coordinate x, Coordinate y) {
        this.x = x;
        this.y = y;
    }

    public boolean isOnX(int x) {
        return this.x.equals(Coordinate.of(x));
    }

    public boolean isOnX(Coordinate x) {
        return this.x.equals(x);
    }

    public boolean isOnY(Coordinate y) {
        return this.y.equals(y);
    }

    public int xDistance(Position position) {
        return this.x.distance(position.x);
    }

    public int yDistance(Position position) {
        return this.y.distance(position.y);
    }

    public boolean hasLargerX(Position position) {
        return this.x.isLargerThan(position.x);
    }

    public static Position of(int x, int y) {
        return positions.stream()
                .filter(position -> position.x == Coordinate.of(x) && position.y == Coordinate.of(y))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 좌표 값입니다."));
    }

    public static Position of(String value) {
        return positions.stream()
                .filter(position -> position.toString().equals(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 좌표 값입니다."));
    }

    public boolean inBetween(final Position start, final Position end) {
        if (isOnX(start.x) && isOnX(end.x)
                && y.isMiddle(start.y, end.y)) {
            return true;
        }

        if (isOnY(start.y) && isOnY(end.y)
                && x.isMiddle(start.x, end.x)) {
            return true;
        }

        return x.distance(start.x) == y.distance(start.y)
                && x.distance(end.x) == y.distance(end.y)
                && y.isMiddle(start.y, end.y)
                && x.isMiddle(start.x, end.x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return y.getAlphabet() + x.toString();
    }
}
