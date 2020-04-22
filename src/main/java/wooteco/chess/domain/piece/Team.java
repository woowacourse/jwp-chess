package wooteco.chess.domain.piece;

public enum Team {
    BLACK("black"),
    WHITE("white");

    private final String value;

    Team(String value) {
        this.value = value;
    }

    public Team negate() {
        if (this == BLACK) {
            return WHITE;
        }
        return BLACK;
    }

    @Override
    public String toString() {
        return value;
    }
}
