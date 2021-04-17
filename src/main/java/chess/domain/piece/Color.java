package chess.domain.piece;

import java.util.Arrays;

public enum Color {
    WHITE("WHITE"),
    BLACK("BLACK");

    private final String value;

    Color(String value) {
        this.value = value;
    }

    public static Color from(final String value) {
        return Arrays.stream(values())
                .filter(color -> color.value.equals(value.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("없는 Color 입니다."));
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

}

