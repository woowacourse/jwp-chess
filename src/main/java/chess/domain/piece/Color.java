package chess.domain.piece;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Color {
    BLACK("black"),
    WHITE("white"),
    EMPTY("empty");

    private static final String NO_MATCHED_COLOR_FOUND_BY_PIECE = "해당 기물의 색상을 확인하는 데 실패하였습니다";
    private static final String NO_MATCHED_COLOR_FOUND_BY_STRING = "일치하는 색상을 찾지 못했습니다";

    private final String colorName;

    Color(String colorName) {
        this.colorName = colorName;
    }

    public static Color from(Piece piece) {
        return Arrays.stream(values())
                .filter(piece::isSameColor)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(NO_MATCHED_COLOR_FOUND_BY_PIECE));
    }

    public static Color from(String colorInput) {
        return Arrays.stream(values())
                .filter(color -> color.colorName.equalsIgnoreCase(colorInput))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(NO_MATCHED_COLOR_FOUND_BY_STRING));
    }

    public Color opposite() {
        if (this == BLACK) {
            return WHITE;
        }
        if (this == WHITE) {
            return BLACK;
        }

        return EMPTY;
    }

    public static Color fromInt(int number) {
        if (number == 0) {
            return WHITE;
        }
        return BLACK;
    }
}
