package wooteco.chess.domain.board;

import java.util.stream.Stream;

public enum Coordinate {
    ONE(1, "a"),
    TWO(2, "b"),
    THREE(3, "c"),
    FOUR(4, "d"),
    FIVE(5, "e"),
    SIX(6, "f"),
    SEVEN(7, "g"),
    EIGHT(8, "h");

    private final int value;
    private final String alphabet;

    Coordinate(int value, String alphabet) {
        this.value = value;
        this.alphabet = alphabet;
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
        return (this.value >= start.value && this.value <= end.value)
                || (this.value <= start.value && this.value >= end.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public String getAlphabet() {
        return alphabet;
    }
}
