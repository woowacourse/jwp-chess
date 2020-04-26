package wooteco.chess.domain.piece;

public enum Color {
    WHITE,
    BLACK,
    NONE;

    public boolean isWhite() {
        return WHITE.equals(this);
    }

    public boolean isBlack() {
        return BLACK.equals(this);
    }

    public boolean isNone() {
        return NONE.equals(this);
    }

    public boolean isSame(Color color) {
        return this.equals(color);
    }
}
