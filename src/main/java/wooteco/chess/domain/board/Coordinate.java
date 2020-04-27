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

    Coordinate(final int value, final String alphabet) {
        this.value = value;
        this.alphabet = alphabet;
    }

    public static Coordinate of(final int value) {
        return Stream.of(Coordinate.values())
                .filter(coordinate -> coordinate.value == value)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 좌표 값입니다."));
    }

    public int distance(final Coordinate compared) {
        return Math.abs(value - compared.value);
    }

    public boolean isLargerThan(final Coordinate coordinate) {
        return this.value > coordinate.value;
    }

    public boolean isMiddle(final Coordinate start, final Coordinate end) {
        return (this.value >= start.value && this.value <= end.value)
                || (this.value <= start.value && this.value >= end.value);
    }

    public String getAlphabet() {
        return alphabet;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
