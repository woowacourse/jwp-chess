package wooteco.chess.domain.board;

import java.util.List;
import java.util.Objects;

public class Position {
    private final Coordinate x;
    private final Coordinate y;

    public static List<Position> positions;

    static {
        for(Coordinate x: Coordinate.values()) {
            for(Coordinate y: Coordinate.values()) {
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

    public static Position of(int x, int y) {
        return positions.stream()
                .filter(position -> position.x == Coordinate.of(x) && position.y == Coordinate.of(y))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 좌표 값입니다."));
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
}
