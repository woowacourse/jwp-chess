package wooteco.chess.domain.piece;

public enum Team {
    BLACK,
    WHITE;

    public Team negate() {
        if (this == BLACK) {
            return WHITE;
        }
        return BLACK;
    }
}
