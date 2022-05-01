package chess.domain.piece;

public enum Name {
    KING("KING"),
    QUEEN("QUEEN"),
    BISHOP("BISHOP"),
    KNIGHT("KNIGHT"),
    ROOK("ROOK"),
    PAWN("PAWN");

    private final String value;

    Name(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean equals(String name) {
        return this.value.equals(name.toUpperCase());
    }
}

