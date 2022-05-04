package chess.dto;

import chess.domain.piece.Piece;

public class PieceDto {

    private String symbol;
    private String color;

    public PieceDto(String symbol, String color) {
        this.symbol = symbol;
        this.color = color;
    }

    public static PieceDto from(Piece piece) {
        return new PieceDto(piece.getSymbol().name(), piece.getColor().name());
    }

    public Piece toPiece() {
        return Piece.of(color, symbol);
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }

    public String getPiece() {
        return color + symbol;
    }
}
