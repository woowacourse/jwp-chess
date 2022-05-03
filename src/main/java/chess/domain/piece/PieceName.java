package chess.domain.piece;

public enum PieceName {
    WHITE_KING("WHITEKING", new King(Color.WHITE)),
    BLACK_KING("BLACKKING", new King(Color.BLACK)),
    WHITE_QUEEN("WHITEQUEEN", new Queen(Color.WHITE)),
    BLACK_QUEEN("BLACKQUEEN", new Queen(Color.BLACK)),
    WHITE_ROOK("WHITEROOK", new Rook(Color.WHITE)),
    BLACK_ROOK("BLACKROOK", new Rook(Color.BLACK)),
    WHITE_KNIGHT("WHITEKNIGHT", new Knight(Color.WHITE)),
    BLACK_KNIGHT("BLACKKNIGHT", new Knight(Color.BLACK)),
    WHITE_BISHOP("WHITEBISHOP", new Bishop(Color.WHITE)),
    BLACK_BISHOP("BLACKBISHOP", new Bishop(Color.BLACK)),
    WHITE_PAWN("WHITEPAWN", new Pawn(Color.WHITE)),
    BLACK_PAWN("BLACKPAWN", new Pawn(Color.BLACK)),
    EMPTY("EMPTYEMPTY", EmptyPiece.getInstance());

    private final String value;
    private final Piece piece;

    PieceName(String value, Piece piece) {
        this.value = value;
        this.piece = piece;
    }

    public String getValue() {
        return value;
    }

    public Piece getPiece() {
        return piece;
    }
}
