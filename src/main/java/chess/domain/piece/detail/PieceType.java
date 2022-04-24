package chess.domain.piece.detail;

public enum PieceType {
    PAWN("P", 1),
    ROOK("R", 5),
    KNIGHT("N", 2.5),
    BISHOP("B", 3),
    QUEEN("Q", 9),
    KING("K", 0),
    BLANK(".", 0),
    ;

    private final String symbol;
    private final double score;

    PieceType(final String symbol, final double score) {
        this.symbol = symbol;
        this.score = score;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getScore() {
        return score;
    }
}
