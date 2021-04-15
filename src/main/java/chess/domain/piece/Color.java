package chess.domain.piece;

import java.util.Arrays;

public enum Color {
    BLACK("BLACK"),
    WHITE("WHITE"),
    NOTHING("NOTHING");

    private final String name;

    Color(final String name) {
        this.name = name;
    }

    public static Color findByValue(final String value) {
        return Arrays.stream(values())
                .filter(targetValue -> targetValue.name.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("없는 색상입니다."));
    }

    public boolean isBlack() {
        return this == BLACK;
    }

    public boolean isOppositeColor(final Color color) {
        if (NOTHING == color) {
            return true;
        }
        return this != color;
    }

    public String getName() {
        return name;
    }
}
