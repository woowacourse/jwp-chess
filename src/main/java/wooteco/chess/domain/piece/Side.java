package wooteco.chess.domain.piece;

public enum Side {
    BLACK,
    WHITE,
    NONE;

    public Side opposite() {
        if (this == BLACK) {
            return WHITE;
        }
        if (this == WHITE) {
            return BLACK;
        }
        return NONE;
    }
}
