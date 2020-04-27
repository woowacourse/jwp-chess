package wooteco.chess.domains.piece;

import java.util.Arrays;

public enum PieceColor {
    BLACK,
    WHITE,
    BLANK;

    private static final String NOT_FOUND_PIECE_COLOR_ERR_MSG = "해당하는 PieceColor가 없습니다.";

    public static PieceColor of(String color) {
        String colorName = color.toUpperCase();

        return Arrays.stream(PieceColor.values())
                .filter(pieceColor -> pieceColor.name().equals(colorName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PIECE_COLOR_ERR_MSG));
    }

    public PieceColor changeTeam() {
        if (this == BLACK) {
            return WHITE;
        }
        return BLACK;
    }
}
