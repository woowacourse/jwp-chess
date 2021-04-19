package chess.domain.board.position;

import chess.domain.piece.movement.Direction;
import chess.domain.piece.movement.Distance;

import java.util.Arrays;

public enum Horizontal {
    ONE(1, "1"),
    TWO(2, "2"),
    THREE(3, "3"),
    FOUR(4, "4"),
    FIVE(5, "5"),
    SIX(6, "6"),
    SEVEN(7, "7"),
    EIGHT(8, "8");

    private final int index;
    private final String horizontal;

    Horizontal(final int index, String horizontal) {
        this.index = index;
        this.horizontal = horizontal;
    }

    public static Horizontal parse(final String number) {
        return Arrays.stream(Horizontal.values())
                .filter(h -> h.index == Integer.parseInt(number))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static Horizontal of(final int index) {
        return Arrays.stream(Horizontal.values())
                .filter(h -> h.index == index)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Horizontal add(final Direction direction, final Distance distance) {
        return of(index + direction.getY() * distance.getValue());
    }

    public int getIndex() {
        return index;
    }

    public String getHorizontal() {
        return horizontal;
    }
}
