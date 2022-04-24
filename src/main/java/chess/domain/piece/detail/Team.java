package chess.domain.piece.detail;

public enum Team {

    WHITE,
    BLACK,
    NONE,
    ;

    public Team reverse() {
        if (this == WHITE) {
            return BLACK;
        }
        if (this == BLACK) {
            return WHITE;
        }
        throw new IllegalStateException("적이 존재할 수 없는 팀입니다.");
    }

    public boolean isBlack() {
        return this == BLACK;
    }

    public boolean isWhite() {
        return this == WHITE;
    }

    public boolean isNone() {
        return this == NONE;
    }
}
