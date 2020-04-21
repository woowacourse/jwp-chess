package wooteco.chess.domain.board;

import java.util.stream.Stream;

public enum Coordinate {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8);

    private final int value;

    Coordinate(int value) {
        this.value = value;
    }

    public static Coordinate of(int value) {
        return Stream.of(Coordinate.values())
                .filter(coordinate -> coordinate.value == value)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 좌표 값입니다."));
    }

    public int distance(Coordinate compared) {
        return Math.abs(value - compared.value);
    }

    public boolean isLargerThan(Coordinate coordinate) {
        return this.value > coordinate.value;
    }

    public boolean isMiddle(Coordinate start, Coordinate end) {
        if ((this.value >= start.value && this.value <= end.value)
                || (this.value <= start.value && this.value >= end.value)) {
            return true;
        }
        return false;
    }
}
