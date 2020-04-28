package wooteco.chess.domains.piece;

public enum PieceType {
    BISHOP("b", 3, "♗", "♝"),
    BLANK(".", 0, "", ""),
    KING("k", 0, "♕", "♛"),
    KNIGHT("n", 2.5, "♘", "♞"),
    PAWN("p", 1, "♙", "♟"),
    QUEEN("q", 9, "♔", "♚"),
    ROOK("r", 5, "♖", "♜");

    private final String name;
    private final double score;
    private final String whiteSymbol;
    private final String blackSymbol;

    PieceType(String name, double score, String whiteSymbol, String blackSymbol) {
        this.name = name;
        this.score = score;
        this.whiteSymbol = whiteSymbol;
        this.blackSymbol = blackSymbol;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public String getWhiteSymbol() {
        return whiteSymbol;
    }

    public String getBlackSymbol() {
        return blackSymbol;
    }
}
