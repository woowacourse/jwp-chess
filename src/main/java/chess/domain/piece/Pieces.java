package chess.domain.piece;

import chess.domain.Team;
import java.util.Arrays;

public enum Pieces {
    BISHOP_BLACK("B", new Bishop(Team.BLACK)),
    BISHOP_WHITE("b", new Bishop(Team.WHITE)),
    KING_BLACK("K", new King(Team.BLACK)),
    KING_WHITE("k",  new King(Team.WHITE)),
    KNIGHT_BLACK("N", new Knight(Team.BLACK)),
    KNIGHT_WHITE("n", new Knight(Team.WHITE)),
    PAWN_BLACK("P", new Pawn(Team.BLACK)),
    PAWN_WHITE("p", new Pawn(Team.WHITE)),
    QUEEN_BLACK("Q", new Queen(Team.BLACK)),
    QUEEN_WHITE("q", new Queen(Team.WHITE)),
    ROOK_BLACK("R", new Rook(Team.BLACK)),
    ROOK_WHITE("r", new Rook(Team.WHITE)),
    BLANK(".", new Blank(Team.NONE));

    private final String symbol;
    private final Piece piece;

    Pieces(String symbol, Piece piece) {
        this.symbol = symbol;
        this.piece = piece;
    }

    public static Piece find(String symbol) {
        return Arrays.stream(values())
            .filter(i -> i.symbol.equals(symbol))
            .map(Pieces::getPiece)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("symbol과 일치하는 말이 존재하지 않습니다."));
    }

    public Piece getPiece() {
        return piece;
    }
}
