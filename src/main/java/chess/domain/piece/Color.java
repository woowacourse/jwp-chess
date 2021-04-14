package chess.domain.piece;


public enum Color {
    BLACK("Black"),
    WHITE("White"),
    NOTHING("");

    private final String name;

    Color(final String name) {
        this.name = name;
    }

    public boolean isBlack() {
        return this == BLACK;
    }
}
