package chess.domain.board.position;

import chess.domain.piece.movement.Direction;
import chess.domain.piece.movement.Distance;

import java.util.Arrays;
import java.util.Locale;

public enum Vertical {
    a(1, "a"),
    b(2, "b"),
    c(3, "c"),
    d(4, "d"),
    e(5, "e"),
    f(6, "f"),
    g(7, "g"),
    h(8, "h");

    private final int index;
    private final String vertical;

    Vertical(final int index, final String vertical) {
        this.index = index;
        this.vertical = vertical;
    }

    public static Vertical parse(final String symbol) {
        return Vertical.valueOf(symbol.toLowerCase(Locale.ROOT));
    }

    public static Vertical of(final int index) {
        return Arrays.stream(Vertical.values())
                .filter(v -> v.index == index)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Vertical add(final Direction direction, final Distance distance) {
        return of(index + direction.getX() * distance.getValue());
    }

    public int getIndex() {
        return index;
    }

    public String getVertical() {
        return vertical;
    }
}
