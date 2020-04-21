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
}
